package com.saemoim.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.saemoim.domain.Group;
import com.saemoim.domain.Participant;
import com.saemoim.domain.User;
import com.saemoim.repository.GroupRepository;
import com.saemoim.repository.ParticipantRepository;
import com.saemoim.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class ParticipantServiceImplTest {
	@Mock
	private ParticipantRepository participantRepository;
	@Mock
	private GroupRepository groupRepository;
	@Mock
	private UserRepository userRepository;
	@InjectMocks
	private ParticipantServiceImpl participantService;

	@Test
	@DisplayName("참여자조회")
	void getParticipants() {
		// given
		var groupId = 1L;
		var group = Group.builder().build();
		when(groupRepository.findById(anyLong())).thenReturn(Optional.of(group));

		// when
		participantService.getParticipants(groupId);

		// then
		verify(participantRepository).findAllByGroupOrderByCreatedAtDesc(group);
	}

	@Test
	@DisplayName("모임탈퇴")
	void withdrawGroup() {
		// given
		var groupId = 1L;
		var userId = 1L;
		var user = User.builder().build();
		var group = Group.builder().build();
		var participant = new Participant(user, group);
		when(groupRepository.findById(anyLong())).thenReturn(Optional.of(group));
		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
		when(participantRepository.findByGroupAndUser(any(Group.class), any(User.class))).thenReturn(
			Optional.of(participant));

		// when
		participantService.withdrawGroup(groupId, userId);

		// then
		verify(participantRepository).delete(any(Participant.class));
	}
}