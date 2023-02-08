package com.saemoim.dto.response;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MyGroupResponseDto {
	private Long id;
	private String groupName;
	private List<ParticipantResponseDto> participants;
}
