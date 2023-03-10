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
	@Column(nullable = false)
	private int likeCount = 0;
	@Column(nullable = true)
	private String imagePath = "/resources/static/images/bird.png";

	public Post(Group group, String title, String content, User user, String imagePath) {
		this.group = group;
		this.title = title;
		this.content = content;
		this.user = user;
		this.imagePath = imagePath;
	}

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

	public void update(String title, String content, String imagePath) {
		this.title = title;
		this.content = content;
		this.imagePath = imagePath;
	}

	public Long getUserId() {
		return this.user.getId();
	}

	public String getUsername() {
		return this.user.getUsername();
	}

	public boolean isWriter(Long userId) {
		return this.user.getId().equals(userId);
	}

	public void plusLikeCount() {
		this.likeCount++;
	}

	public void minusLikeCount() {
		this.likeCount--;
	}
}
