package com.saemoim.dto.response;

import com.saemoim.domain.Group;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MyGroupResponseDto {
	private Long id;
	private String groupName;

	public MyGroupResponseDto(Group group) {
		this.id = group.getId();
		this.groupName = group.getName();
	}
}
