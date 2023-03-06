package com.saemoim.chat.dto;

import java.time.LocalDateTime;

import com.saemoim.chat.domain.Chat;

import lombok.Getter;

@Getter
public class ChatResponseDto {
	private Long groupId;
	private Long userId;
	private String writer;
	private String message;
	private LocalDateTime createdAt;

	public ChatResponseDto(Chat chat) {
		this.groupId = chat.getGroupId();
		this.userId = chat.getUserId();
		this.writer = chat.getWriter();
		this.message = chat.getMessage();
		this.createdAt = chat.getCreatedAt();
	}

	public ChatResponseDto(Long groupId, Long userId, String writer, String message, LocalDateTime createdAt) {
		this.groupId = groupId;
		this.userId = userId;
		this.writer = writer;
		this.message = message;
		this.createdAt = createdAt;
	}
}
