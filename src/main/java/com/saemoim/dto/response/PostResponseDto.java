package com.saemoim.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.saemoim.domain.Post;

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

	public PostResponseDto(Long id, String title, String username, String content, LocalDateTime createdAt,
		LocalDateTime modifiedAt) {
		this.id = id;
		this.title = title;
		this.username = username;
		this.content = content;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;

	}

	public PostResponseDto(Post savedPost) {
		this.id = savedPost.getId();
		this.title = savedPost.getTitle();
		this.username = savedPost.getUser().getUsername();
		this.content = savedPost.getContent();
		this.createdAt = savedPost.getCreatedAt();
		this.modifiedAt = savedPost.getModifiedAt();
	}
}
