package com.saemoim.dto.response;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BlackListResponseDto {
	private Long id;
	private Long userId;
	private String username;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;
}
