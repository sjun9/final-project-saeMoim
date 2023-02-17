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
public class PostLike {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OnDelete(action= OnDeleteAction.CASCADE)
	@JoinColumn(name = "post_id", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Post post;

	@Column(nullable = false)
	private Long userId;

	public PostLike(Post post, Long userId) {
		this.post = post;
		this.userId = userId;
	}
}
