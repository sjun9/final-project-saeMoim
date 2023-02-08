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
}
