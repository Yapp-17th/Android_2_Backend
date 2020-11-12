package com.yapp.crew.domain.repository;

import static com.yapp.crew.domain.model.QBoard.board;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yapp.crew.domain.condition.BoardFilterCondition;
import com.yapp.crew.domain.condition.BoardSearchCondition;
import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.model.QHiddenBoard;
import java.util.List;
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
		BooleanBuilder builder = new BooleanBuilder();

		if (boardSearchCondition.getKeywords() != null) {
			builder.or(isSearched(boardSearchCondition.getKeywords()));
		}

		return jpaQueryFactory
				.selectFrom(board)
				.where(builder, isHidden(boardSearchCondition.getUserId()))
				.fetch();
	}

	public List<Board> filter(BoardFilterCondition boardFilterCondition) {
		BooleanBuilder builder = new BooleanBuilder();

		if (boardFilterCondition.getCategory() != null) {
			builder.or(isFilteredCategory(boardFilterCondition.getCategory()));
		}

		if (boardFilterCondition.getCity() != null) {
			builder.or(isFilteredCity(boardFilterCondition.getCity()));
		}

		return jpaQueryFactory
				.selectFrom(board)
				.where(builder, isHidden(boardFilterCondition.getUserId()))
				.fetch();
	}

	// TODO: List가 들어올 때에는 BooleanBuilder -> BooleanExpression 으로 표현할 방법 생각해보기
//	private BooleanBuilder isFiltered(List<Long> category, List<Long> city) {
//		return isFilteredCategory(category).or(isFilteredCity(city));
//	}

	private BooleanBuilder isFilteredCategory(List<Long> categories) {
		BooleanBuilder builder = new BooleanBuilder();
		for (Long category : categories) {
			builder.or(board.category.id.eq(category));
		}
		return builder;
	}

	private BooleanBuilder isFilteredCity(List<Long> cities) {
		BooleanBuilder builder = new BooleanBuilder();
		for (Long city : cities) {
			builder.or(board.address.id.eq(city));
		}
		return builder;
	}

	private BooleanExpression isHidden(Long userId) {
		return QHiddenBoard.hiddenBoard.user.id.eq(userId);
	}

	private BooleanBuilder isSearched(List<String> keywords) {
		BooleanBuilder builder = new BooleanBuilder();
		for (String keyword : keywords) {
			builder.or(board.content.containsIgnoreCase(keyword));
		}
		return builder;
	}
}
