package com.yapp.crew.domain.repository;

import static com.yapp.crew.domain.model.QBoard.board;
import static com.yapp.crew.domain.model.QHiddenBoard.hiddenBoard;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yapp.crew.domain.condition.BoardFilterCondition;
import com.yapp.crew.domain.condition.BoardSearchCondition;
import com.yapp.crew.domain.model.Board;
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

	/*
	 * SELECT *
	 *	FROM board as b left join hidden_board as hb
	 *	on (b.id = hb.board_id and hb.user_id=1)
	 *	where hb.user_id is null; */

	public List<Board> search(BoardSearchCondition boardSearchCondition, Pageable pageable) {
		return jpaQueryFactory.select(board)
				.from(board).leftJoin(hiddenBoard).on(
						board.id.eq(hiddenBoard.board.id).and(hiddenBoard.user.id.eq(boardSearchCondition.getUserId())))
				.where(
						hiddenBoard.user.id.isNull(),
						isSearchedKeywords(boardSearchCondition.getKeywords()),
						isDeletedBoard()
				).orderBy(board.createdAt.desc())
				.fetch();
	}

	public List<Board> filter(BoardFilterCondition boardFilterCondition, Pageable pageable) {
		return jpaQueryFactory.select(board)
				.from(board).leftJoin(hiddenBoard).on(
						board.id.eq(hiddenBoard.board.id).and(hiddenBoard.user.id.eq(boardFilterCondition.getUserId())))
				.where(
						hiddenBoard.user.id.isNull(),
						isFilteredCategories(boardFilterCondition.getCategory()),
						isFilteredCities(boardFilterCondition.getCity()),
						isDeletedBoard()
				)
				.orderBy(orderType(boardFilterCondition.getSorting()))
				.fetch();
	}

	private OrderSpecifier<?> orderType(SortingType sortingType) {
		if (sortingType == SortingType.REMAIN) {
			// TODO: 남은 인원수 정렬... 이거 어떻게 하지? --> 걍 마지막에 하면 pagenation을 어떡하지...
			return board.appliedUsers.size().asc();
		} else if (sortingType == SortingType.DEADLINE) {
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
