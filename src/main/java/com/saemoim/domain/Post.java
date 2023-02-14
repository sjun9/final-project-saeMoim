package com.saemoim.domain;

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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User User;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "group_id")
	private Group group;

	@Column(nullable = false)
	private String title;
	@Column(nullable = false)
	private String content;

	public Post(Long groupId, String title, String content, String username) {
	// 뭔가 꼬였다
	/*
		1. 받아오는 건 groupId 인데 객체를 입력해야 하는 아이러니한 상황
		2. 서비스단에서 group을 group 아이디로 조회하고 그걸 넣어야 하나?
		3. 그냥 group id 째로 인풋하면 안 되는건가?
	 */
	}
}
