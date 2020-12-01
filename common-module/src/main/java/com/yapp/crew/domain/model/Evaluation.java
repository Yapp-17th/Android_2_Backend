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
	@JoinColumn(name = "board_id", nullable = false)
	private Board board;

	@Column(name = "evaluate_id", nullable = false)
	private Long evaluateId;

	@Column(name = "evaluated_id", nullable = false)
	private Long evaluatedId;

	@Column(name = "is_like", nullable = false)
	private Boolean isLike = false;

	@Column(name = "is_dislike", nullable = false)
	private Boolean isDislike = false;

	public void evaluate(boolean isLike){
		this.isLike=isLike;
		this.isDislike=!isLike;
	}

	public static EvaluationBuilder getBuilder() {
		return new EvaluationBuilder();
	}

	public static class EvaluationBuilder {

		private Long evaluateId;
		private Long evaluatedId;
		private Board board;
		private Boolean isLike;
		private Boolean isDislike;

		public EvaluationBuilder withBoard(Board board) {
			this.board = board;
			return this;
		}

		public EvaluationBuilder withEvaluateId(Long evaluateId) {
			this.evaluateId = evaluateId;
			return this;
		}

		public EvaluationBuilder withEvaluatedId(Long evaluatedId) {
			this.evaluatedId = evaluatedId;
			return this;
		}

		public EvaluationBuilder withIsLike(Boolean isLike) {
			this.isLike = isLike;
			return this;
		}

		public EvaluationBuilder withIsDislike(Boolean isDislike) {
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
		return this.getId().toString();
	}
}
