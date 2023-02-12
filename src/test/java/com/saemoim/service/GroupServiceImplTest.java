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
	@DisplayName("그룹생성")
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
		Category category = Category.builder().id(1L).layer(1).parentId(1L).name("named").build();
		when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));

		// when
		GroupResponseDto response = groupService.createGroup(request, user);

		// then
		assertThat(response.getGroupName()).isEqualTo("name");
		assertThat(response.getUsername()).isEqualTo("name");
		verify(categoryRepository).findById(anyLong());
		verify(groupRepository).save(any(Group.class));
	}
}