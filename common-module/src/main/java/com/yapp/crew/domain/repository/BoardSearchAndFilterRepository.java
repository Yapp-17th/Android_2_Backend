package com.yapp.crew.domain.repository;

import static com.yapp.crew.domain.model.QBoard.board;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yapp.crew.domain.condition.BoardFilterCondition;
import com.yapp.crew.domain.condition.BoardSearchCondition;
import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.type.SortingType;
import java.util.List;
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
						isSearchedKeywords(boardSearchCondition.getKeywords())
				).orderBy(board.createdAt.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetchResults();

		return new PageImpl<>(boardQueryResults.getResults(), pageable, boardQueryResults.getTotal());
	}

	public Page<Board> filter(BoardFilterCondition boardFilterCondition, Pageable pageable) {
		BooleanBuilder booleanBuilder = new BooleanBuilder();
		if (boardFilterCondition.getCategory() != null) {
			booleanBuilder.or(isFilteredCategories(boardFilterCondition.getCategory()));
		}
		if (boardFilterCondition.getCity() != null) {
			booleanBuilder.or(isFilteredCities(boardFilterCondition.getCity()));
		}

		QueryResults<Board> boardQueryResults = jpaQueryFactory
				.selectFrom(board)
			  .where(booleanBuilder)
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

	private BooleanBuilder isFilteredCategories(List<Long> categories) {
		BooleanBuilder builder = new BooleanBuilder();
		for (Long categoryId : categories) {
			builder.or(isFilteredCategory(categoryId));
		}
		return builder;
	}

	private BooleanBuilder isFilteredCategory(Long categoryId) {
		BooleanBuilder builder = new BooleanBuilder();
		builder.or(board.category.id.eq(categoryId));
		return builder;
	}

	private BooleanBuilder isFilteredCities(List<Long> cities) {
		BooleanBuilder builder = new BooleanBuilder();
		for (Long cityId : cities) {
			builder.or(isFilteredCity(cityId));
		}
		return builder;
	}

	private BooleanBuilder isFilteredCity(Long cityId) {
		BooleanBuilder builder = new BooleanBuilder();
		builder.or(board.address.id.eq(cityId));
		return builder;
	}

	private BooleanBuilder isSearchedKeywords(List<String> keywords) {
		BooleanBuilder builder = new BooleanBuilder();
		for (String keyword : keywords) {
			builder.and(isSearchedKeyword(keyword));
		}
		return builder;
	}

	private BooleanBuilder isSearchedKeyword(String keyword) {
		BooleanBuilder builder = new BooleanBuilder();
		builder.or(board.content.containsIgnoreCase(keyword));
		return builder;
	}

}
