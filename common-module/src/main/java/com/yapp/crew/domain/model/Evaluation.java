package com.yapp.crew.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Evaluation extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private Board board;

	@Column(name = "evaluate_id", nullable = false)
	private long evaluateId;

	@Column(name = "evaluated_id", nullable = false)
	private long evaluatedId;

	@Column(name = "is_like", nullable = false)
	private boolean isLike;

	@Column(name = "is_dislike", nullable = false)
	private boolean isDislike;

	public void evaluate(boolean isLike){
		this.isLike = isLike;
		this.isDislike = !isLike;
	}

	public static EvaluationBuilder getBuilder() {
		return new EvaluationBuilder();
	}

	public static class EvaluationBuilder {
		private long evaluateId;
		private long evaluatedId;
		private Board board;
		private boolean isLike;
		private boolean isDislike;

		public EvaluationBuilder withBoard(Board board) {
			this.board = board;
			return this;
		}

		public EvaluationBuilder withEvaluateId(long evaluateId) {
			this.evaluateId = evaluateId;
			return this;
		}

		public EvaluationBuilder withEvaluatedId(long evaluatedId) {
			this.evaluatedId = evaluatedId;
			return this;
		}

		public EvaluationBuilder withIsLike(boolean isLike) {
			this.isLike = isLike;
			return this;
		}

		public EvaluationBuilder withIsDislike(boolean isDislike) {
			this.isDislike = isDislike;
			return this;
		}

		public Evaluation build() {
			Evaluation evaluation = new Evaluation();
			evaluation.board = board;
			evaluation.evaluatedId = evaluatedId;
			evaluation.evaluateId = evaluateId;
			evaluation.isLike = isLike;
			evaluation.isDislike = isDislike;

			return evaluation;
		}
	}

	@Override
	public String toString() {
		return String.valueOf(this.getId());
	}
}
