package com.yapp.crew.domain.repository;

import static com.yapp.crew.domain.model.QBoard.board;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yapp.crew.domain.condition.BoardFilterCondition;
import com.yapp.crew.domain.condition.BoardSearchCondition;
import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.model.QHiddenBoard;
import com.yapp.crew.domain.type.SortingType;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class BoardSearchAndFilterRepository {

	private final JPAQueryFactory jpaQueryFactory;

	public BoardSearchAndFilterRepository(EntityManager entityManager) {
		this.jpaQueryFactory = new JPAQueryFactory(entityManager);
	}

	public Page<Board> search(BoardSearchCondition boardSearchCondition, Pageable pageable) {
		QueryResults<Board> boardQueryResults = jpaQueryFactory
				.selectFrom(board)
				.where(
						isSearchedKeywords(boardSearchCondition.getKeywords()),
						isHidden(boardSearchCondition.getUserId())
				)
				.orderBy(board.createdAt.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetchResults();

		return new PageImpl<>(boardQueryResults.getResults(), pageable, boardQueryResults.getTotal());
	}

	public Page<Board> filter(BoardFilterCondition boardFilterCondition, Pageable pageable) {
		QueryResults<Board> boardQueryResults = jpaQueryFactory
				.selectFrom(board)
				.where(
						isFilteredCategories(boardFilterCondition.getCategory()),
						isFilteredCities(boardFilterCondition.getCity()),
						isHidden(boardFilterCondition.getUserId())
				)
				.orderBy(orderType(boardFilterCondition.getSorting()))
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetchResults();

		return new PageImpl<>(boardQueryResults.getResults(), pageable, boardQueryResults.getTotal());
	}

	private OrderSpecifier<?> orderType(SortingType sortingType) {
		if (sortingType == SortingType.REMAIN) {
			// TODO: 남은 인원수 정렬... 이거 어떻게 하지?
			return board.appliedUsers.size().asc();
		} else if (sortingType == SortingType.DEADLINE) {
			return board.startsAt.asc();
		}
		return board.createdAt.desc();
	}

	private BooleanExpression isFilteredCategories(List<Long> categories) {
		return Expressions.anyOf((BooleanExpression) categories.stream().map(this::isFilteredCategory).collect(Collectors.toList()));
	}

	private BooleanExpression isFilteredCategory(Long categoryId) {
		return board.category.id.eq(categoryId);
	}

	private BooleanExpression isFilteredCities(List<Long> cities) {
		return Expressions.anyOf((BooleanExpression) cities.stream().map(this::isFilteredCity).collect(Collectors.toList()));
	}

	private BooleanExpression isFilteredCity(Long cityId) {
		return board.address.id.eq(cityId);
	}

	private BooleanExpression isSearchedKeywords(List<String> keywords) {
		return Expressions.allOf((BooleanExpression) keywords.stream().map(this::isSearchedKeyword).collect(Collectors.toList()));
	}

	private BooleanExpression isSearchedKeyword(String keyword) {
		return board.content.containsIgnoreCase(keyword);
	}

	private BooleanExpression isHidden(Long userId) {
		return QHiddenBoard.hiddenBoard.user.id.eq(userId);
	}
}
