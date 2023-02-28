package com.saemoim.dto.response;

import java.time.LocalDateTime;

import com.saemoim.domain.Post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponseDto {
	private Long id;
	private String title;
	private Long userId;
	private String username;
	private String content;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;
	private int likeCount;
	private boolean isLikeChecked;

	public PostResponseDto(Post post) {
		this.id = post.getId();
		this.userId = post.getUserId();
		this.title = post.getTitle();
		this.username = post.getUsername();
		this.content = post.getContent();
		this.createdAt = post.getCreatedAt();
		this.modifiedAt = post.getModifiedAt();
		this.likeCount = post.getLikeCount();
	}
}
