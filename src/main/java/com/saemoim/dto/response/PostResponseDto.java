package com.saemoim.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostResponseDto {
	private Long id;
	private String title;
	private Long userId;
	private String username;
	private String content;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;
	private List<CommentResponseDto> comments;

	public PostResponseDto(Long id, String title, String username, String content, LocalDateTime createdAt,
		LocalDateTime modifiedAt, List<CommentResponseDto> commentResponseDtos) {
		this.id = id;
		this.title = title;
		this.username = username;
		this.content = content;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;

	}
}
