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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Post extends TimeStamped {
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
	private String title;
	@Column(nullable = false)
	private String content;

	public Post(Group group, String title, String content, User user) {
		this.group = group;
		this.title = title;
		this.content = content;
		this.user = user;
	}

	public void update(String title, String content) {
		this.title = title;
		this.content = content;
	}

	public Long getUserId() {
		return this.user.getId();
	}

	public boolean isWriter(Long userId) {
		return this.user.getId().equals(userId);
	}
}
