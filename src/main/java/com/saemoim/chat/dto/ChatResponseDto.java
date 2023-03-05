package com.saemoim.chat.dto;

import com.saemoim.chat.domain.Chat;

import lombok.Getter;

@Getter
public class ChatResponseDto {
	private Long groupId;
	private String writer;
	private String message;

	public ChatResponseDto(Chat chat) {
		this.groupId = chat.getGroupId();
		this.writer = chat.getWriter();
		this.message = chat.getMessage();
	}

	public ChatResponseDto(Long groupId, String writer, String message) {
		this.groupId = groupId;
		this.writer = writer;
		this.message = message;
	}
}
