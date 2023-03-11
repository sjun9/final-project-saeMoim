package com.saemoim.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saemoim.domain.Participant;
import com.saemoim.dto.response.ParticipantResponseDto;
import com.saemoim.exception.ErrorCode;
import com.saemoim.repository.ParticipantRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ParticipantServiceImpl implements ParticipantService {
	private final ParticipantRepository participantRepository;

	@Transactional(readOnly = true)
	@Override
	public List<ParticipantResponseDto> getParticipants(Long groupId) {
		return participantRepository.findAllByGroup_IdOrderByCreatedAtDesc(groupId)
			.stream().map(ParticipantResponseDto::new).toList();
	}

	@Transactional
	@Override
	public void withdrawGroup(Long groupId, Long userId) {
		Participant participant = participantRepository.findByGroup_IdAndUser_Id(groupId, userId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_PARTICIPANT.getMessage())
		);
		participantRepository.delete(participant);
		participant.getGroup().subtractParticipantCount();
	}
}
