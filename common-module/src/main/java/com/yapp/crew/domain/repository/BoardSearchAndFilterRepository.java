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
				.orderBy(board.status.asc(), board.createdAt.desc()) // 최신순 정렬
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
	}

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
				.orderBy(board.status.asc(), orderType(boardFilterCondition.getSorting()))
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
		if(categories.contains(0L)){
			return null;
		}

		return Expressions.anyOf(categories.stream().map(this::isFilteredCategory).toArray(BooleanExpression[]::new));
	}

	private BooleanExpression isFilteredCategory(Long categoryId) {
		return board.category.id.eq(categoryId);
	}

	private BooleanExpression isFilteredCities(List<Long> cities) {
		if(cities.contains(0L)){
			return null;
		}

		return Expressions.anyOf(cities.stream().map(this::isFilteredCity).toArray(BooleanExpression[]::new));
	}

	private BooleanExpression isFilteredCity(long cityId) {
		return board.address.id.eq(cityId);
	}

	private BooleanExpression isSearchedKeywords(List<String> keywords) {
		return Expressions.anyOf(keywords.stream().map(keyword -> {
				BooleanExpression isContent = isSearchedKeywordInContent(keyword);
				BooleanExpression isTitle = isSearchedKeywordInTitle(keyword);
				BooleanExpression isNickname = isSearchedKeywordInUserNickname(keyword);

				return Expressions.anyOf(isContent, isTitle, isNickname);
		}).toArray(BooleanExpression[]::new));
	}

	private BooleanExpression isSearchedKeywordInContent(String keyword) {
		return board.content.containsIgnoreCase(keyword);
	}

	private BooleanExpression isSearchedKeywordInTitle(String keyword) {
		return board.title.containsIgnoreCase(keyword);
	}

	private BooleanExpression isSearchedKeywordInUserNickname(String keyword) {
		return board.user.nickname.containsIgnoreCase(keyword);
	}

	private BooleanExpression isDeletedBoard() {
		return board.status.ne(BoardStatus.CANCELED);
	}
}
