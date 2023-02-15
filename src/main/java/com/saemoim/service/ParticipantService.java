package com.saemoim.service;

import java.util.List;

import com.saemoim.dto.response.ParticipantResponseDto;

public interface ParticipantService {
	List<ParticipantResponseDto> getParticipants(Long groupId);

	void withdrawGroup(Long participantId, String username);
}
