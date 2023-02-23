package com.saemoim.dto.response;

import java.time.LocalDateTime;
import java.util.List;

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
	public PostResponseDto(Post savedPost) {
		this.id = savedPost.getId();
		this.userId = savedPost.getUserId();
		this.title = savedPost.getTitle();
		this.username = savedPost.getUsername();
		this.content = savedPost.getContent();
		this.createdAt = savedPost.getCreatedAt();
		this.modifiedAt = savedPost.getModifiedAt();
		this.likeCount = savedPost.getLikeCount();
	}
}
