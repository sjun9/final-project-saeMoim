package com.saemoim.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostListResponseDto {

	private Long id;
	private String title;
	private String username;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;
}
