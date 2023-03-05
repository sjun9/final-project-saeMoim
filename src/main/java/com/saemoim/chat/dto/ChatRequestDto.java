package com.saemoim.chat.dto;

import lombok.Getter;

@Getter
public class ChatRequestDto {
	private Long groupId;
	private String writer;
	private String message;

}
