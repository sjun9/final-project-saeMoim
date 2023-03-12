package com.saemoim.dto.response;

import com.saemoim.domain.Participant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantResponseDto {
	private Long userId;
	private String username;

	public ParticipantResponseDto(Participant participant) {
		this.userId = participant.getUserId();
		this.username = participant.getUsername();
	}
}
