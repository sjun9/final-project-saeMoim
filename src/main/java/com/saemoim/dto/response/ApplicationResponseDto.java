package com.saemoim.dto.response;

import com.saemoim.domain.enums.ApplicationStatusEnum;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApplicationResponseDto {
	private Long id;
	private Long groupId;
	private String groupName;
	private Long userId;
	private String username;
	private ApplicationStatusEnum status;
}
