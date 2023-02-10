package com.saemoim.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saemoim.dto.response.StatusResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ParticipantServiceImpl implements ParticipantService {
	@Transactional
	@Override
	public StatusResponseDto withdrawGroup(Long participantId, String username) {
		return null;
	}
}
