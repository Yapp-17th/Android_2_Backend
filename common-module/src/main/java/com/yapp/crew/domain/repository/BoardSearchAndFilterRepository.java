package com.yapp.crew.domain.repository;

import static com.yapp.crew.domain.model.QAppliedUser.appliedUser;
import static com.yapp.crew.domain.model.QBoard.board;
import static com.yapp.crew.domain.model.QHiddenBoard.hiddenBoard;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yapp.crew.domain.condition.BoardFilterCondition;
import com.yapp.crew.domain.condition.BoardSearchCondition;
import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.status.AppliedStatus;
import com.yapp.crew.domain.status.BoardStatus;
import com.yapp.crew.domain.type.SortingType;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class BoardSearchAndFilterRepository {

	private final JPAQueryFactory jpaQueryFactory;

	public BoardSearchAndFilterRepository(EntityManager entityManager) {
		this.jpaQueryFactory = new JPAQueryFactory(entityManager);
	}

	public List<Board> search(BoardSearchCondition boardSearchCondition, Pageable pageable) {
		return jpaQueryFactory.select(board)
				.from(board).leftJoin(hiddenBoard)
				.on(board.id.eq(hiddenBoard.board.id).and(hiddenBoard.user.id.eq(boardSearchCondition.getUserId())))
				.where(
						hiddenBoard.user.id.isNull(),
						isSearchedKeywords(boardSearchCondition.getKeywords()),
						isDeletedBoard()
				)
				.orderBy(board.createdAt.desc()) // 최신순 정렬
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
	}

	/*
	 * SELECT HIDDEN_FILTERED.*, COALESCE(APPROVED_USER.approved_number, 0) as approved_number, (HIDDEN_FILTERED.recruit_count - COALESCE(APPROVED_USER.approved_number, 0)) as remain_number
	 * FROM (SELECT b.* FROM board as b left join hidden_board as hb on (b.id = hb.board_id and hb.user_id=1) where hb.user_id is null) as HIDDEN_FILTERED
	 * left join (select board_id, count(user_id) as approved_number from applied_user where status='APPROVED' group by board_id) as APPROVED_USER
	 * on HIDDEN_FILTERED.id = APPROVED_USER.board_id
	 * order by approved_number DESC;
	 * */

	/*
	 * SELECT b.*, (b.recruit_count - COALESCE(count(au.user_id), 0)) as remain_count
	 * FROM (board as b left join hidden_board as hb on b.id = hb.board_id and hb.user_id=1) left join applied_user as au on b.id = au.board_id and au.status = 'APPROVED'
	 * where hb.user_id is null
	 * group by b.id
	 * order by remain_count DESC;
	 * */

	public List<Board> filter(BoardFilterCondition boardFilterCondition, Pageable pageable) {
		return jpaQueryFactory
				.select(board)
				.from(board)
				.leftJoin(hiddenBoard).on(board.id.eq(hiddenBoard.board.id).and(hiddenBoard.user.id.eq(boardFilterCondition.getUserId())))
				.leftJoin(appliedUser).on(board.id.eq(appliedUser.board.id).and(appliedUser.status.eq(AppliedStatus.APPROVED)))
				.where(
						hiddenBoard.user.id.isNull(),
						isFilteredCategories(boardFilterCondition.getCategory()),
						isFilteredCities(boardFilterCondition.getCity()),
						isDeletedBoard()
				)
				.groupBy(board.id)
				.orderBy(orderType(boardFilterCondition.getSorting()))
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
	}

	private OrderSpecifier<?> orderType(SortingType sortingType) {
		if(sortingType == SortingType.REMAIN){
			return board.recruitCount.subtract(appliedUser.user.id.count().coalesce(0L)).desc();
		}
		if (sortingType == SortingType.DEADLINE) {
			return board.startsAt.asc();
		}
		return board.createdAt.desc();
	}

	private BooleanExpression isFilteredCategories(List<Long> categories) {
		return categories != null ? Expressions.anyOf(categories.stream().map(this::isFilteredCategory).toArray(BooleanExpression[]::new)) : null;
	}

	private BooleanExpression isFilteredCategory(Long categoryId) {
		return board.category.id.eq(categoryId);
	}

	private BooleanExpression isFilteredCities(List<Long> cities) {
		return cities != null ? Expressions.anyOf(cities.stream().map(this::isFilteredCity).toArray(BooleanExpression[]::new)) : null;
	}

	private BooleanExpression isFilteredCity(Long cityId) {
		return board.address.id.eq(cityId);
	}

	private BooleanExpression isSearchedKeywords(List<String> keywords) {
		return Expressions.allOf(keywords.stream().map(this::isSearchedKeyword).toArray(BooleanExpression[]::new));
	}

	private BooleanExpression isSearchedKeyword(String keyword) {
		return board.content.containsIgnoreCase(keyword);
	}

	private BooleanExpression isDeletedBoard() {
		return board.status.ne(BoardStatus.CANCELED);
	}
}
