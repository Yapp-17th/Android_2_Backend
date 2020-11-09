package com.yapp.crew.domain.repository;

import static com.yapp.crew.domain.model.QBoard.*;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yapp.crew.domain.condition.BoardFilterCondition;
import com.yapp.crew.domain.condition.BoardSearchCondition;
import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.model.QBoard;
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

	public List<Board> search(BoardSearchCondition boardSearchCondition) {
		JPAQuery<Board> where = jpaQueryFactory
				.selectFrom(board)
				.where();

	}

	public List<Board> filter(BoardFilterCondition boardFilterCondition) {
		JPAQuery<Board> where = jpaQueryFactory
				.selectFrom(board)
				.where();

	}

	private BooleanExpression isFiltered(List<Long> category, List<Long> city) {

	}

	private BooleanExpression isHidden(Long userId) {
		return QHiddenBoard.hiddenBoard
	}

	private BooleanExpression isSearched(List<String> keyword) {
		Expression<String>
		return board.content.containsIgnoreCase(Expressions.string);
	}
}
