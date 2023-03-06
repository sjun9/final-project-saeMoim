package com.saemoim.dto.response;

import java.time.LocalDateTime;

import com.saemoim.domain.BlackList;
import com.saemoim.domain.enums.BlacklistStatusEnum;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BlackListResponseDto {
	private Long id;
	private Long userId;
	private String username;
	private int banCount;
	private BlacklistStatusEnum status;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;

	public BlackListResponseDto(BlackList blackList) {
		this.id = blackList.getId();
		this.userId = blackList.getUserId();
		this.username = blackList.getUsername();
		this.banCount = blackList.getBanCount();
		this.createdAt = blackList.getCreatedAt();
		this.modifiedAt = blackList.getModifiedAt();
		this.status = blackList.getStatus();
	}
}
