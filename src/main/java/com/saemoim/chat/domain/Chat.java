package com.saemoim.chat.domain;

import com.saemoim.chat.dto.ChatRequestDto;
import com.saemoim.domain.TimeStamped;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Chat extends TimeStamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long userId;

	@Column(nullable = false)
	private Long groupId;

	@Column(nullable = false)
	private String writer;

	@Column(nullable = false)
	private String message;

	public Chat(ChatRequestDto chatMessage) {
		this.userId = chatMessage.getUserId();
		this.groupId = chatMessage.getGroupId();
		this.writer = chatMessage.getWriter();
		this.message = chatMessage.getMessage();
	}
}
