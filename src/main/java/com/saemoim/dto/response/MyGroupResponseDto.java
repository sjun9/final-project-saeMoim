package com.saemoim.dto.response;

import java.util.List;

import com.saemoim.domain.Group;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MyGroupResponseDto {
	private Long id;
	private String groupName;
	private List<ParticipantResponseDto> participants;

	public MyGroupResponseDto(Group group, List<ParticipantResponseDto> participants) {
		this.id = group.getId();
		this.groupName = group.getName();
		this.participants = participants;
	}
}
