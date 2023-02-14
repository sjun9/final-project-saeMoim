package com.saemoim.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saemoim.domain.Group;
import com.saemoim.domain.Participant;
import com.saemoim.dto.response.ParticipantResponseDto;
import com.saemoim.exception.ErrorCode;
import com.saemoim.repository.GroupRepository;
import com.saemoim.repository.ParticipantRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ParticipantServiceImpl implements ParticipantService {

	private final ParticipantRepository participantRepository;
	private final GroupRepository groupRepository;

	@Transactional(readOnly = true)
	@Override
	public List<ParticipantResponseDto> getParticipants(Long groupId) {
		Group group = groupRepository.findById(groupId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_GROUP.getMessage())
		);
		List<ParticipantResponseDto> participantResponseDto = new ArrayList<>();
		List<Participant> participants = participantRepository.findAllByGroupOrderByCreatedAtDesc(group);
		participants.forEach(participant -> participantResponseDto.add(new ParticipantResponseDto(participant)));
		return participantResponseDto;
	}

	@Transactional
	@Override
	public void withdrawGroup(Long participantId, String username) {

	}
}
