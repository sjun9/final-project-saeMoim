package com.saemoim.dto.response;

import java.time.LocalDateTime;

import com.saemoim.domain.Comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponseDto {
	private Long id;
	private String comment;
	private Long userId;
	private String username;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;

	public CommentResponseDto(Comment savedComment) {
		this.id = savedComment.getId();
		this.comment = savedComment.getComment();
		this.userId = savedComment.getUser().getId();
		this.username = savedComment.getUser().getUsername();
		this.createdAt = savedComment.getCreatedAt();
		this.modifiedAt = savedComment.getModifiedAt();
	}
}
