package com.saemoim.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ParticipantResponseDto {
	private Long id;
	private Long userId;
	private String username;
}
