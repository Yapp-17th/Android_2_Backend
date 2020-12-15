package com.yapp.crew.domain.repository;

import static com.yapp.crew.domain.model.QAppliedUser.appliedUser;
import static com.yapp.crew.domain.model.QBoard.board;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yapp.crew.domain.condition.HistoryCondition;
import com.yapp.crew.domain.model.Board;
import com.yapp.crew.domain.status.AppliedStatus;
import com.yapp.crew.domain.status.BoardStatus;
import com.yapp.crew.domain.type.HistoryType;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class UserProfileHistoryRepository {

	private final JPAQueryFactory jpaQueryFactory;

	public UserProfileHistoryRepository(EntityManager entityManager) {
		this.jpaQueryFactory = new JPAQueryFactory(entityManager);
	}

	public List<Board> getHistory(HistoryCondition historyCondition, Pageable pageable){
		return jpaQueryFactory.select(board)
				.from(board)
				.leftJoin(appliedUser).on(board.id.eq(appliedUser.board.id).and(appliedUser.user.id.eq(historyCondition.getUserId())))
				.where(
						isBoardHost(historyCondition.getUserId()).or(isBoardApprovedGuest()),
						filterBoardStatus(historyCondition.getHistoryType())
				)
				.orderBy(appliedUser.createdAt.desc()) // 최신순 정렬
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
	}

	private BooleanExpression isBoardHost(long userId) {
		return board.user.id.eq(userId);
	}

	private BooleanExpression isBoardApprovedGuest() {
		return appliedUser.status.eq(AppliedStatus.APPROVED);
	}

	private BooleanExpression filterBoardStatus(HistoryType historyType){
		if(historyType == HistoryType.COMPLETE){
			return board.status.eq(BoardStatus.CANCELED).or(board.status.eq(BoardStatus.FINISHED));
		}

		return board.status.eq(BoardStatus.RECRUITING).or(board.status.eq(BoardStatus.COMPLETE));
	}
}
