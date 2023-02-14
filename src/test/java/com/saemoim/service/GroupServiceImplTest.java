package com.saemoim.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.saemoim.domain.Category;
import com.saemoim.domain.Group;
import com.saemoim.domain.User;
import com.saemoim.domain.enums.GroupStatusEnum;
import com.saemoim.domain.enums.UserRoleEnum;
import com.saemoim.dto.request.GroupRequestDto;
import com.saemoim.dto.response.GroupResponseDto;
import com.saemoim.repository.CategoryRepository;
import com.saemoim.repository.GroupRepository;

@ExtendWith(MockitoExtension.class)
class GroupServiceImplTest {

	@Mock
	private GroupRepository groupRepository;

	@Mock
	private CategoryRepository categoryRepository;

	@InjectMocks
	private GroupServiceImpl groupService;

	@Test
	@DisplayName("모임생성")
	void createGroup() {
		// given
		GroupRequestDto request = GroupRequestDto.builder()
			.categoryId(1L)
			.tagNames(new ArrayList<>())
			.address("djemfotm")
			.firstRegion("d")
			.secondRegion("22")
			.latitude("soqkq")
			.longitude("ddddd")
			.name("name")
			.content("contenddddddddddddddddt")
			.recruitNumber(3)
			.build();
		User user = new User("dddd", "ddd", "name", UserRoleEnum.LEADER);
		Category category = Category.builder().parentId(1L).name("named").build();
		when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));

		// when
		GroupResponseDto response = groupService.createGroup(request, user);

		// then
		assertThat(response.getGroupName()).isEqualTo("name");
		assertThat(response.getUsername()).isEqualTo("name");
		verify(categoryRepository).findById(anyLong());
		verify(groupRepository).save(any(Group.class));
	}

	@Test
	@DisplayName("모임수정")
	void updateGroup() {
		// given
		GroupRequestDto request = GroupRequestDto.builder()
			.categoryId(1L)
			.tagNames(new ArrayList<>())
			.address("djemfotm")
			.firstRegion("d")
			.secondRegion("22")
			.latitude("soqkq")
			.longitude("ddddd")
			.name("nana")
			.content("contenddddddddddddddddt")
			.recruitNumber(3)
			.build();
		User user = new User("email", "pass", "john", UserRoleEnum.LEADER);
		Group group = Group.builder().id(1L).user(user).name("name").build();
		Category category = Category.builder().parentId(1L).name("named").build();
		Group mockGroup = mock(Group.class);

		when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
		when(groupRepository.findById(anyLong())).thenReturn(Optional.of(group));

		// when
		GroupResponseDto response = groupService.updateGroup(group.getId(), request, user.getUsername());

		// then
		assertThat(response.getGroupName()).isEqualTo(group.getName());
	}

	@Test
	@DisplayName("모임삭제")
	void deleteGroup() {
		// given
		User user = new User("email", "pass", "john", UserRoleEnum.LEADER);
		Group group = Group.builder().id(1L).user(user).name("name").build();

		when(groupRepository.findById(anyLong())).thenReturn(Optional.of(group));
		// when
		groupService.deleteGroup(group.getId(), user.getUsername());

		// then
		verify(groupRepository).delete(group);
	}

	@Test
	@DisplayName("모임열기")
	void openGroup() {
		// given
		var user = new User("email", "pass", "name", UserRoleEnum.LEADER);
		var group = Group.builder().status(GroupStatusEnum.CLOSE).user(user).build();

		when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));

		// when
		groupService.openGroup(group.getId(), user.getUsername());

		// then
		assertThat(group.getStatus()).isEqualTo(GroupStatusEnum.OPEN);
	}

	@Test
	@DisplayName("모임닫기")
	void closeGroup() {
		// given
		var user = new User("email", "pass", "name", UserRoleEnum.LEADER);
		var group = Group.builder().status(GroupStatusEnum.OPEN).user(user).build();

		when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));

		// when
		groupService.closeGroup(group.getId(), user.getUsername());

		// then
		assertThat(group.getStatus()).isEqualTo(GroupStatusEnum.CLOSE);
	}

}