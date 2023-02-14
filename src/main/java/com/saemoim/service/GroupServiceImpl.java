package com.saemoim.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saemoim.domain.Category;
import com.saemoim.domain.Group;
import com.saemoim.domain.Participant;
import com.saemoim.domain.User;
import com.saemoim.domain.enums.GroupStatusEnum;
import com.saemoim.dto.request.GroupRequestDto;
import com.saemoim.dto.response.GroupResponseDto;
import com.saemoim.dto.response.MyGroupResponseDto;
import com.saemoim.exception.ErrorCode;
import com.saemoim.repository.CategoryRepository;
import com.saemoim.repository.GroupRepository;
import com.saemoim.repository.ParticipantRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

	private final GroupRepository groupRepository;
	private final CategoryRepository categoryRepository;
	private final ParticipantRepository participantRepository;

	@Override
	@Transactional(readOnly = true)
	public Page<GroupResponseDto> getAllGroups(Pageable pageable) {
		List<Group> groups = groupRepository.findAllByOrderByCreatedAtDesc(pageable);
		List<GroupResponseDto> groupResponseDto = new ArrayList<>();
		groups.forEach(group -> groupResponseDto.add(new GroupResponseDto(group)));
		return new PageImpl<>(groupResponseDto);
	}

	@Override
	@Transactional(readOnly = true)
	public GroupResponseDto getGroup(Long groupId) {
		Group group = groupRepository.findById(groupId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_GROUP.getMessage())
		);
		return new GroupResponseDto(group);
	}

	@Override
	@Transactional(readOnly = true)
	public List<MyGroupResponseDto> getMyGroupsByLeader(User user) {
		List<MyGroupResponseDto> myGroupResponseDtoList = new ArrayList<>();
		List<Group> groups = groupRepository.findAllByUserOrderByCreatedAtDesc(user);
		groups.forEach(group -> myGroupResponseDtoList.add(new MyGroupResponseDto(group)));

		return myGroupResponseDtoList;
	}

	@Override
	@Transactional(readOnly = true)
	public List<MyGroupResponseDto> getMyGroupsByParticipant(User user) {
		List<MyGroupResponseDto> myGroupResponseDtoList = new ArrayList<>();

		List<Group> groups = participantRepository.findAllByUserOrderByCreatedAtDesc(user)
			.stream()
			.map(Participant::getGroup)
			.toList();
		groups.forEach(group -> myGroupResponseDtoList.add(new MyGroupResponseDto(group)));
		return myGroupResponseDtoList;
	}

	@Override
	@Transactional
	public GroupResponseDto createGroup(GroupRequestDto requestDto, User user) {
		// 카테고리 존재 확인
		Category category = categoryRepository.findById(requestDto.getCategoryId()).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_EXIST_CATEGORY.getMessage())
		);
		if (category.getParentId() == null) {
			throw new IllegalArgumentException(ErrorCode.NOT_PARENT_CATEGORY.getMessage());
		}
		Group group = new Group(requestDto, category, user);
		groupRepository.save(group);

		return new GroupResponseDto(group);
	}

	@Override
	@Transactional
	public GroupResponseDto updateGroup(Long groupId, GroupRequestDto requestDto, String username) {
		// 카테고리 존재 확인
		Category category = categoryRepository.findById(requestDto.getCategoryId()).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_EXIST_CATEGORY.getMessage())
		);
		if (category.getParentId() == null) {
			throw new IllegalArgumentException(ErrorCode.NOT_PARENT_CATEGORY.getMessage());
		}
		Group group = groupRepository.findById(groupId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_GROUP.getMessage())
		);
		if (group.getUser().getUsername().equals(username)) {
			group.update(requestDto, category, group.getUser());
			groupRepository.save(group);
		} else
			throw new IllegalArgumentException(ErrorCode.INVALID_USER.getMessage());

		return new GroupResponseDto(group);
	}

	@Override
	@Transactional
	public void deleteGroup(Long groupId, String username) {
		Group group = groupRepository.findById(groupId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_GROUP.getMessage())
		);
		if (group.getUser().getUsername().equals(username)) {
			groupRepository.delete(group);
		} else {
			throw new IllegalArgumentException(ErrorCode.INVALID_USER.getMessage());
		}
	}

	@Override
	@Transactional
	public void openGroup(Long groupId, String username) {
		Group group = groupRepository.findById(groupId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_GROUP.getMessage())
		);
		if (group.getStatus().equals(GroupStatusEnum.OPEN)) {
			throw new IllegalArgumentException(ErrorCode.ALREADY_OPEN.getMessage());
		}
		if (group.getUser().getUsername().equals(username))
			group.updateStatusToOpen();

		else {
			throw new IllegalArgumentException(ErrorCode.INVALID_USER.getMessage());
		}
	}

	@Transactional
	@Override
	public void closeGroup(Long groupId, String username) {
		Group group = groupRepository.findById(groupId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_GROUP.getMessage())
		);
		if (group.getStatus().equals(GroupStatusEnum.CLOSE)) {
			throw new IllegalArgumentException(ErrorCode.ALREADY_CLOSE.getMessage());
		}
		if (group.getUser().getUsername().equals(username))
			group.updateStatusToClose();
		else {
			throw new IllegalArgumentException(ErrorCode.INVALID_USER.getMessage());
		}
	}
}
