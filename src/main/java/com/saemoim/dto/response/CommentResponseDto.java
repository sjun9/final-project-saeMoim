package com.saemoim.dto.response;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentResponseDto {
	private Long id;
	private String comment;
	private Long userId;
	private String username;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;

	public CommentResponseDto(String writer, String comment1, LocalDateTime createdAt1, LocalDateTime modifiedAt1) {
		this.username = writer;
		this.comment = comment1;
		this.createdAt = createdAt1;
		this.modifiedAt = modifiedAt1;
	}
}
