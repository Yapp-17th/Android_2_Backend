package com.yapp.crew.domain.repository;

import static com.yapp.crew.domain.model.QBoard.board;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yapp.crew.domain.condition.BoardFilterCondition;
import com.yapp.crew.domain.condition.BoardSearchCondition;
import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.model.QHiddenBoard;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class BoardSearchAndFilterRepository {

	private final JPAQueryFactory jpaQueryFactory;

	public BoardSearchAndFilterRepository(EntityManager entityManager) {
		this.jpaQueryFactory = new JPAQueryFactory(entityManager);
	}

	// TODO: sorting 추가
	public List<Board> search(BoardSearchCondition boardSearchCondition) {
		return jpaQueryFactory
				.selectFrom(board)
				.where(
						isSearchedKeywords(boardSearchCondition.getKeywords()),
						isHidden(boardSearchCondition.getUserId())
				)
				.fetch();
	}

	public List<Board> filter(BoardFilterCondition boardFilterCondition) {
		return jpaQueryFactory
				.selectFrom(board)
				.where(
						isFilteredCategories(boardFilterCondition.getCategory()),
						isFilteredCities(boardFilterCondition.getCity()),
						isHidden(boardFilterCondition.getUserId())
				)
				.fetch();
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
