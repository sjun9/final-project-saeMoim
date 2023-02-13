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
import com.saemoim.domain.Review;
import com.saemoim.domain.User;
import com.saemoim.domain.enums.GroupStatusEnum;
import com.saemoim.dto.request.GroupRequestDto;
import com.saemoim.dto.response.GroupResponseDto;
import com.saemoim.dto.response.MyGroupResponseDto;
import com.saemoim.dto.response.ParticipantResponseDto;
import com.saemoim.dto.response.ReviewResponseDto;
import com.saemoim.exception.ErrorCode;
import com.saemoim.repository.CategoryRepository;
import com.saemoim.repository.GroupRepository;
import com.saemoim.repository.ParticipantRepository;
import com.saemoim.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

	private final GroupRepository groupRepository;
	private final CategoryRepository categoryRepository;
	private final ReviewRepository reviewRepository;
	private final ParticipantRepository participantRepository;

	@Override
	@Transactional(readOnly = true)
	public Page<GroupResponseDto> getAllGroups(Pageable pageable) {
		List<Group> groups = groupRepository.findAllByOrderByCreatedAtDesc(pageable);
		List<Long> allGroupIdList = groups.stream().map(Group::getId).toList();
		List<Review> reviews = reviewRepository.findAllReviewsByGroupId(allGroupIdList);
		// api 분리
		List<GroupResponseDto> groupResponseDto = new ArrayList<>();
		for (Group group : groups) {
			List<ReviewResponseDto> reviewList = reviews.stream()
				.filter(r -> r.getGroupId().equals(group.getId()))
				.map(ReviewResponseDto::new)
				.toList();
			groupResponseDto.add(new GroupResponseDto(group, reviewList));
		}
		return new PageImpl<>(groupResponseDto);
	}

	@Override
	@Transactional(readOnly = true)
	public GroupResponseDto getGroup(Long groupId) {
		Group group = groupRepository.findById(groupId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_GROUP.getMessage())
		);
		List<ReviewResponseDto> reviewList = reviewRepository.findAllByGroupOrderByCreatedAtDesc(group)
			.stream()
			.map(ReviewResponseDto::new)
			.toList();
		return new GroupResponseDto(group, reviewList);
	}

	@Override
	@Transactional(readOnly = true)
	public List<MyGroupResponseDto> getMyGroupsByLeader(User user) {  // 리더아이디

		List<MyGroupResponseDto> myGroupResponseDtoList = new ArrayList<>();

		List<Group> groups = groupRepository.findAllByUserOrderByCreatedAtDesc(user);
		addMyGroupResponseDtoList(myGroupResponseDtoList, groups);

		return myGroupResponseDtoList;
	}

	@Transactional(readOnly = true)
	@Override
	public List<MyGroupResponseDto> getMyGroupsByParticipant(User user) {  // 참가자아이디
		List<MyGroupResponseDto> myGroupResponseDtoList = new ArrayList<>();

		List<Group> groups = participantRepository.findAllByUserOrderByCreatedAtDesc(user)
			.stream()
			.map(Participant::getGroup)
			.toList();

		addMyGroupResponseDtoList(myGroupResponseDtoList, groups);
		return myGroupResponseDtoList;
	}

	private void addMyGroupResponseDtoList(List<MyGroupResponseDto> myGroupResponseDtoList, List<Group> groups) {
		List<Long> groupIdList = groups.stream().map(Group::getId).toList();

		List<Participant> participants = participantRepository.findAllParticipants(groupIdList);
		// api 분리
		for (Group group : groups) {
			List<ParticipantResponseDto> participantResponseDtoList = participants.stream()
				.filter(p -> p.getGroup().getId().equals(group.getId()))
				.map(ParticipantResponseDto::new)
				.toList();
			myGroupResponseDtoList.add(new MyGroupResponseDto(group, participantResponseDtoList));
		}
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

		return new GroupResponseDto(group, new ArrayList<>());
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

		return new GroupResponseDto(group, new ArrayList<>());
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
