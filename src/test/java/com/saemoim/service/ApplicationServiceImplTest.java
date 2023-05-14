package com.saemoim.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.saemoim.domain.Application;
import com.saemoim.domain.Group;
import com.saemoim.domain.Participant;
import com.saemoim.domain.User;
import com.saemoim.domain.enums.ApplicationStatusEnum;
import com.saemoim.domain.enums.UserRoleEnum;
import com.saemoim.repository.ApplicationRepository;
import com.saemoim.repository.GroupRepository;
import com.saemoim.repository.ParticipantRepository;
import com.saemoim.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceImplTest {

	@Mock
	private ApplicationRepository applicationRepository;
	@Mock
	private UserRepository userRepository;
	@Mock
	private GroupRepository groupRepository;
	@Mock
	private ParticipantRepository participantRepository;
	@InjectMocks
	private ApplicationServiceImpl applicationService;

	@Test
	@DisplayName("내가 신청한 내역 조회")
	void getMyApplications() {
		// given
		var userId = 1L;
		var user = new User("email", "pass", "name", UserRoleEnum.USER);
		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

		// when
		applicationService.getMyApplications(userId);

		// then
		verify(applicationRepository).findAllByUserOrderByCreatedAtDesc(user);
	}

	@Test
	@DisplayName("모임참여신청")
	void applyGroup() {
		// given
		var userId = 1L;
		var groupId = 1L;
		var user = User.builder().id(1L).username("pati").role(UserRoleEnum.USER).build();
		var user2 = User.builder().id(2L).username("pati2").role(UserRoleEnum.USER).build();
		var group = Group.builder().user(user).name("group").build();
		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user2));
		when(groupRepository.findById(anyLong())).thenReturn(Optional.of(group));
		when(applicationRepository.existsByUserAndGroup(any(User.class), any(Group.class))).thenReturn(false);
		// when
		applicationService.applyGroup(groupId, userId);

		// then
		verify(applicationRepository).save(any(Application.class));
	}

	@Test
	@DisplayName("참여신청취소")
	void cancelApplication() {
		// given
		Long applicationId = 1L;
		Long userId = 1L;
		var user = User.builder().id(1L).username("pati").role(UserRoleEnum.USER).build();
		var application = Application.builder().id(1L).user(user).build();

		when(applicationRepository.findById(anyLong())).thenReturn(Optional.of(application));
		// when
		applicationService.deleteApplication(applicationId, userId);

		// then
		verify(applicationRepository).delete(application);

	}

	@Test
	@DisplayName("리더가모임요청내역조회")
	void getApplications() {
		// given
		var userId = 1L;
		var group = Group.builder().user(new User("e", "p", "nana", UserRoleEnum.USER)).build();
		List<Group> list = new ArrayList<>();
		list.add(group);
		when(groupRepository.findAllByUser_Id(anyLong())).thenReturn(list);

		// when
		applicationService.getApplications(userId);

		// then
		verify(applicationRepository).findAllByGroups(list);

	}

	@Test
	@DisplayName("신청수락")
	void permitApplication() {
		// given
		var applicationId = 1L;
		var userId = 1L;
		User user = User.builder().id(1L).username("pati").role(UserRoleEnum.USER).build();
		Group group = Group.builder().id(1L).user(user).build();
		Application application = Application.builder()
			.group(group)
			.user(user)
			.build();

		when(applicationRepository.findById(anyLong())).thenReturn(Optional.of(application));
		when(groupRepository.findById(anyLong())).thenReturn(Optional.of(group));
		when(userRepository.findById(1L)).thenReturn(Optional.of(application.getUser()));
		// when
		applicationService.permitApplication(applicationId, userId);
		// then
		verify(applicationRepository).save(any(Application.class));
		verify(participantRepository).save(any(Participant.class));
		assertThat(application.getStatus()).isEqualTo(ApplicationStatusEnum.PERMIT);
	}

	@Test
	@DisplayName("신청거절")
	void rejectApplication() {
		// given
		var applicationId = 1L;
		var userId = 1L;
		User user = User.builder().id(1L).username("pati").build();
		Group group = Group.builder().id(1L).user(user).build();
		Application application = Application.builder().status(ApplicationStatusEnum.WAIT)
			.group(group)
			.user(user)
			.build();

		when(applicationRepository.findById(anyLong())).thenReturn(Optional.of(application));
		when(groupRepository.findById(anyLong())).thenReturn(Optional.of(group));
		// when
		applicationService.rejectApplication(applicationId, userId);
		// then
		verify(applicationRepository).save(any(Application.class));
		assertThat(application.getStatus()).isEqualTo(ApplicationStatusEnum.REJECT);
	}
}