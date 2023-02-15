package com.saemoim.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saemoim.domain.Group;
import com.saemoim.domain.Participant;
import com.saemoim.domain.User;
import com.saemoim.dto.response.ParticipantResponseDto;
import com.saemoim.exception.ErrorCode;
import com.saemoim.repository.GroupRepository;
import com.saemoim.repository.ParticipantRepository;
import com.saemoim.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ParticipantServiceImpl implements ParticipantService {

	private final ParticipantRepository participantRepository;
	private final GroupRepository groupRepository;
	private final UserRepository userRepository;

	@Transactional(readOnly = true)
	@Override
	public List<ParticipantResponseDto> getParticipants(Long groupId) {
		Group group = groupRepository.findById(groupId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_GROUP.getMessage())
		);
		List<Participant> participants = participantRepository.findAllByGroupOrderByCreatedAtDesc(group);
		return participants.stream().map(ParticipantResponseDto::new).toList();
	}

	@Transactional
	@Override
	public void withdrawGroup(Long groupId, Long userId) {
		Group group = groupRepository.findById(groupId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_GROUP.getMessage())
		);
		User user = userRepository.findById(userId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage())
		);
		Participant participant = participantRepository.findByGroupAndUser(group, user).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_PARTICIPANT.getMessage())
		);
		participantRepository.delete(participant);
	}
}
