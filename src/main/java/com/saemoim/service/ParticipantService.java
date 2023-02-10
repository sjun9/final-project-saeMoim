package com.saemoim.service;

import com.saemoim.dto.response.StatusResponseDto;

public interface ParticipantService {
	StatusResponseDto withdrawGroup(Long participantId, String username);
}
