package com.yapp.crew.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
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

	@ManyToOne
	@JoinColumn(name = "board_id")
	private Board board;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "is_like", nullable = false)
	private Boolean isLike = false;

	@Column(name = "is_dislike", nullable = false)
	private Boolean isDislike = false;

}
