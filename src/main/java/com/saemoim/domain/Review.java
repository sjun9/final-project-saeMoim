package com.saemoim.domain;

import com.saemoim.dto.request.ReviewRequestDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Review extends TimeStamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "group_id")
	private Group group;

	@Column(nullable = false)
	private String content;

	public Review(ReviewRequestDto requestDto, Group group, User user) {
		this.user = user;
		this.group = group;
		this.content = requestDto.getComment();
	}

	public Long getGroupId() {
		return this.group.getId();
	}

	public Long getUserId() {
		return this.user.getId();
	}

	public String getUsername() {
		return this.user.getUsername();
	}
}
