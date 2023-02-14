package com.saemoim.service;

import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.saemoim.domain.Application;
import com.saemoim.domain.Group;
import com.saemoim.domain.User;
import com.saemoim.domain.enums.UserRoleEnum;
import com.saemoim.repository.ApplicationRepository;
import com.saemoim.repository.GroupRepository;
import com.saemoim.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceImplTest {

	@Mock
	private ApplicationRepository applicationRepository;
	@Mock
	private UserRepository userRepository;
	@Mock
	private GroupRepository groupRepository;
	@InjectMocks
	private ApplicationServiceImpl applicationService;

	@Test
	@DisplayName("내가 신청한 내역 조회")
	void getMyApplications() {
		// given
		var username = "name";
		var user = new User("email", "pass", "name", UserRoleEnum.USER);
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

		// when
		applicationService.getMyApplications(username);

		// then
		verify(applicationRepository).findAllByUserOrderByCreatedAtDesc(user);
	}

	@Test
	@DisplayName("모임참여신청")
	void applyGroup() {
		// given
		var group = Group.builder().id(1L).name("group").build();
		var user = new User("email", "pass", "name", UserRoleEnum.USER);
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
		when(groupRepository.findById(anyLong())).thenReturn(Optional.of(group));
		when(applicationRepository.existsByUserAndGroup(any(User.class), any(Group.class))).thenReturn(false);
		// when
		applicationService.applyGroup(group.getId(), user.getUsername());

		// then
		verify(applicationRepository).save(any(Application.class));
	}

	@Test
	@DisplayName("참여신청취소")
	void cancelApplication() {
		// given
		Long applicationId = 1L;
		String username = "name";
		var user = new User("email", "pass", "name", UserRoleEnum.USER);
		var application = Application.builder().id(1L).user(user).build();

		when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
		when(applicationRepository.findById(anyLong())).thenReturn(Optional.of(application));
		// when
		applicationService.cancelApplication(applicationId, username);

		// then
		verify(applicationRepository).delete(application);

	}

	@Test
	@DisplayName("리더가모임요청내역조회")
	void getApplications() {
		// given
		var groupId = 1L;
		var username = "nana";
		var group = Group.builder().user(new User("e", "p", "nana", UserRoleEnum.LEADER)).build();
		when(groupRepository.findById(anyLong())).thenReturn(Optional.of(group));

		// when
		applicationService.getApplications(groupId, username);

		// then
		verify(applicationRepository).findAllByGroupOrderByCreatedAt(group);

	}

	@Test
	void permitApplication() {
	}

	@Test
	void rejectApplication() {
	}
}