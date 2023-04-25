package com.saemoim.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.web.multipart.MultipartFile;

import com.saemoim.domain.Category;
import com.saemoim.domain.Group;
import com.saemoim.domain.Participant;
import com.saemoim.domain.Tag;
import com.saemoim.domain.User;
import com.saemoim.domain.enums.GroupStatusEnum;
import com.saemoim.domain.enums.UserRoleEnum;
import com.saemoim.dto.request.GroupRequestDto;
import com.saemoim.dto.response.GroupResponseDto;
import com.saemoim.fileUpload.AWSS3Uploader;
import com.saemoim.repository.CategoryRepository;
import com.saemoim.repository.GroupRepository;
import com.saemoim.repository.ParticipantRepository;
import com.saemoim.repository.TagRepository;
import com.saemoim.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class GroupServiceImplTest {
	@Mock
	private GroupRepository groupRepository;
	@Mock
	private UserRepository userRepository;
	@Mock
	private CategoryRepository categoryRepository;
	@Mock
	private ParticipantRepository participantRepository;
	@Mock
	private TagRepository tagRepository;
	@Mock
	private AWSS3Uploader awss3Uploader;
	@InjectMocks
	private GroupServiceImpl groupService;

	@Test
	@DisplayName("전체 모임 조회")
	void getAllGroups() {
		//given
		Long groupId = 5L;
		PageRequest pageable = PageRequest.of(0, 5);
		Slice<Group> groups = new SliceImpl<>(new ArrayList<>(), pageable, true);

		when(groupRepository.findAllByOrderByCreatedAtDesc(groupId, pageable)).thenReturn(groups);
		//when
		Slice<GroupResponseDto> response = groupService.getAllGroups(groupId, pageable);
		//then
		verify(groupRepository).findAllByOrderByCreatedAtDesc(groupId, pageable);
	}

	@Test
	@DisplayName("상세 모임 조회")
	void getGroup() {
		//given
		Long groupId = 1L;
		Group group = mock(Group.class);
		when(group.getName()).thenReturn("농구하자");
		when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
		//when
		GroupResponseDto response = groupService.getGroup(groupId);
		//then
		assertThat(response.getGroupName()).isEqualTo("농구하자");
	}

	@Test
	@DisplayName("인기 모임 조회")
	void getGroupByPopularity() {
		//given
		Group group1 = mock(Group.class);
		Group group2 = mock(Group.class);
		Group group3 = mock(Group.class);
		List<Group> groups = new ArrayList<>();
		groups.add(group1);
		groups.add(group2);
		groups.add(group3);
		when(groupRepository.findAll()).thenReturn(groups);
		when(group1.getWishCount()).thenReturn(1);
		when(group2.getWishCount()).thenReturn(2);
		when(group3.getWishCount()).thenReturn(3);
		when(group1.getViews()).thenReturn(20);
		when(group2.getViews()).thenReturn(25);
		when(group3.getViews()).thenReturn(10);
		when(group1.getName()).thenReturn("group1");
		when(group2.getName()).thenReturn("group2");
		when(group3.getName()).thenReturn("group3");
		//when
		List<GroupResponseDto> response = groupService.getGroupByPopularity();
		//then
		assertThat(response.get(0).getGroupName()).isEqualTo("group2");
		assertThat(response.get(1).getGroupName()).isEqualTo("group3");
		assertThat(response.get(2).getGroupName()).isEqualTo("group1");
	}

	@Test
	@DisplayName("태그 모임 조회")
	void searchGroupsByTag() {
		//given
		String tagName = "tag";
		PageRequest pageable = PageRequest.of(0, 5);
		Slice<Tag> tags = new SliceImpl<>(new ArrayList<>(), pageable, true);

		when(tagRepository.findAllByNameContaining(tagName, pageable)).thenReturn(tags);
		//when
		Slice<GroupResponseDto> response = groupService.searchGroupsByTag(tagName, pageable);
		//then
		verify(tagRepository).findAllByNameContaining(tagName, pageable);
	}

	@Test
	@DisplayName("검색 모임 조회")
	void searchGroups() {
		//given
		String groupName = "group";
		PageRequest pageable = PageRequest.of(0, 5);
		Slice<Group> groups = new SliceImpl<>(new ArrayList<>(), pageable, true);

		when(groupRepository.findAllByNameContainingOrderByCreatedAtDesc(groupName, pageable)).thenReturn(groups);
		//when
		Slice<GroupResponseDto> response = groupService.searchGroups(groupName, pageable);
		//then
		verify(groupRepository).findAllByNameContainingOrderByCreatedAtDesc(groupName, pageable);
	}

	@Test
	@DisplayName("내가 생성한 모임 조회")
	void getMyGroupsByLeader() {
		//given
		Long userId = 1L;
		List<Group> groups = new ArrayList<>();

		when(groupRepository.findAllByUser_IdOrderByCreatedAtDesc(userId)).thenReturn(groups);
		//when
		List<GroupResponseDto> response = groupService.getMyGroupsByLeader(userId);
		//then
		verify(groupRepository).findAllByUser_IdOrderByCreatedAtDesc(userId);
	}

	@Test
	@DisplayName("내가 참여한 모임 조회")
	void getMyGroupsByParticipant() {
		//given
		Long userId = 1L;
		List<Participant> participants = new ArrayList<>();

		when(participantRepository.findAllByUser_IdOrderByCreatedAtDesc(userId)).thenReturn(participants);
		//when
		List<GroupResponseDto> response = groupService.getMyGroupsByParticipant(userId);
		//then
		verify(participantRepository).findAllByUser_IdOrderByCreatedAtDesc(userId);
	}

	@Test
	@DisplayName("모임생성")
	void createGroup() {
		// given
		GroupRequestDto request = GroupRequestDto.builder()
			.categoryName("named")
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
		var id = 1L;
		User user = new User("dddd", "ddd", "name", UserRoleEnum.USER);
		Category category = Category.builder().parentId(1L).name("named").build();
		MultipartFile multipartFile = mock(MultipartFile.class);
		Group group = mock(Group.class);

		when(categoryRepository.findByName(anyString())).thenReturn(Optional.of(category));
		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
		when(groupRepository.save(any(Group.class))).thenReturn(group);
		doNothing().when(group).addParticipantCount();
		try {
			when(awss3Uploader.upload(multipartFile, "group")).thenReturn("aaaaaa");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		// when
		groupService.createGroup(request, id, multipartFile);
		// then
		verify(categoryRepository).findByName(anyString());
		verify(groupRepository).save(any(Group.class));
		verify(participantRepository).save(any(Participant.class));
	}

	@Test
	@DisplayName("모임수정")
	void updateGroup() {
		// given
		GroupRequestDto request = GroupRequestDto.builder()
			.categoryName("named")
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
		var id = 1L;
		var userId = 1L;
		var imgPath = mock(MultipartFile.class);
		User user = mock(User.class);
		Group group = Group.builder().id(userId).user(user).name("name").build();
		Category category = Category.builder().parentId(1L).name("named").build();

		when(user.getId()).thenReturn(1L);
		when(categoryRepository.findByName(anyString())).thenReturn(Optional.of(category));
		when(groupRepository.findById(anyLong())).thenReturn(Optional.of(group));

		// when
		groupService.updateGroup(id, request, userId, imgPath);

		// then
		verify(groupRepository).save(any(Group.class));
	}

	@Test
	@DisplayName("모임삭제")
	void deleteGroup() {
		// given
		Long userId = 1L;
		User user = mock(User.class);
		Group group = Group.builder().id(1L).user(user).name("name").build();

		when(groupRepository.findById(anyLong())).thenReturn(Optional.of(group));
		when(user.getId()).thenReturn(userId);
		// when
		groupService.deleteGroup(group.getId(), userId);

		// then
		verify(groupRepository).delete(group);
	}

	@Test
	@DisplayName("모임열기")
	void openGroup() {
		// given
		var userId = 1L;
		var user = mock(User.class);
		var group = Group.builder().status(GroupStatusEnum.CLOSE).user(user).build();

		when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));
		when(user.getId()).thenReturn(userId);
		// when
		groupService.openGroup(group.getId(), userId);

		// then
		assertThat(group.getStatus()).isEqualTo(GroupStatusEnum.OPEN);
	}

	@Test
	@DisplayName("모임닫기")
	void closeGroup() {
		// given
		var userId = 1L;
		var user = mock(User.class);
		var group = Group.builder().status(GroupStatusEnum.OPEN).user(user).build();

		when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));
		when(user.getId()).thenReturn(userId);

		// when
		groupService.closeGroup(group.getId(), userId);

		// then
		assertThat(group.getStatus()).isEqualTo(GroupStatusEnum.CLOSE);
	}

}