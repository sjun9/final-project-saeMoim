package com.saemoim.dto.response;

import com.saemoim.domain.Application;
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

	public ApplicationResponseDto(Application application) {
		this.id = application.getId();
		this.groupId = application.getGroupId();
		this.groupName = application.getGroupName();
		this.userId = application.getUserId();
		this.username = application.getUsername();
		this.status = application.getStatus();
	}
}
