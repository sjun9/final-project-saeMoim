package com.saemoim.domain;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Review extends TimeStamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OnDelete(action = OnDeleteAction.CASCADE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@OnDelete(action = OnDeleteAction.CASCADE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "group_id")
	private Group group;

	@Column(nullable = false)
	private String content;

	public Review(Participant participant, String content) {
		this.user = participant.getUser();
		this.group = participant.getGroup();
		this.content = content;
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

	public void update(String content) {
		this.content = content;
	}

	public boolean isReviewWriter(String username) {
		return this.user.getUsername().equals(username);
	}
}
