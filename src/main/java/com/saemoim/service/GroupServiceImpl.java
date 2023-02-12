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
import com.saemoim.dto.request.GroupRequestDto;
import com.saemoim.dto.response.GroupResponseDto;
import com.saemoim.dto.response.MyGroupResponseDto;
import com.saemoim.dto.response.ParticipantResponseDto;
import com.saemoim.dto.response.ReviewResponseDto;
import com.saemoim.dto.response.StatusResponseDto;
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
		List<ReviewResponseDto> reviewList = reviewRepository.findAllByGroupIdOrderByCreatedAt(groupId)
			.stream()
			.map(ReviewResponseDto::new)
			.toList();
		return new GroupResponseDto(group, reviewList);
	}

	@Override
	@Transactional(readOnly = true)
	public List<MyGroupResponseDto> getMyGroupsByLeader(Long userId) {  // 리더아이디

		List<MyGroupResponseDto> myGroupResponseDtoList = new ArrayList<>();

		List<Group> groups = groupRepository.findAllByUserIdOrderByCreatedAtDesc(userId);
		List<Long> groupIdList = groups
			.stream()
			.map(Group::getId)
			.toList();

		List<Participant> participants = participantRepository.findAllParticipants(groupIdList);

		for (Group group : groups) {
			List<ParticipantResponseDto> participantResponseDtoList = participants.stream()
				.filter(p -> p.getGroupId().equals(group.getId()))
				.map(ParticipantResponseDto::new)
				.toList();
			myGroupResponseDtoList.add(new MyGroupResponseDto(group, participantResponseDtoList));
		}

		return myGroupResponseDtoList;
	}

	@Transactional(readOnly = true)
	@Override
	public List<MyGroupResponseDto> getMyGroupsByParticipant(Long userId) {  // 참가자아이디
		List<MyGroupResponseDto> myGroupResponseDtoList = new ArrayList<>();

		List<Group> groups = participantRepository.findByUserId(userId)
			.stream()
			.map(Participant::getGroup)
			.toList();

		List<Participant> participants = participantRepository.findAllParticipants(
			groups.stream().map(Group::getId).toList());

		for (Group group : groups) {
			List<ParticipantResponseDto> participantResponseDtoList = participants.stream()
				.filter(p -> p.getGroupId().equals(group.getId()))
				.map(ParticipantResponseDto::new)
				.toList();
			myGroupResponseDtoList.add(new MyGroupResponseDto(group, participantResponseDtoList));
		}
		return myGroupResponseDtoList;
	}

	@Override
	@Transactional
	public GroupResponseDto createGroup(GroupRequestDto requestDto, User user) {
		// 카테고리 존재 확인
		Category category = categoryRepository.findById(requestDto.getCategoryId()).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_CATEGORY.getMessage())
		);
		if (category.getParentId() == null) {
			throw new IllegalArgumentException(ErrorCode.NOT_CHILD_CATEGORY.getMessage());
		}
		Group group = new Group(requestDto, category, user);
		groupRepository.save(group);

		return new GroupResponseDto(group, new ArrayList<>());
	}

	@Transactional
	@Override
	public GroupResponseDto updateGroup(Long groupId, GroupRequestDto requestDto, String username) {
		// 그룹을 가져오기
		// 그룹을 가져오면 태그리스트를 갖고옴 원투매니 했지롱
		return null;
	}

	@Transactional
	@Override
	public StatusResponseDto deleteGroup(Long groupId, String username) {
		// 그룹도 지우고
		// 참가자도 지우고
		// 태그도 지우고 -> 연결되어있으니까 한번에 지워지겠지?
		return null;
	}

	@Transactional
	@Override
	public StatusResponseDto openGroup(Long groupId, String username) {
		return null;
	}

	@Transactional
	@Override
	public StatusResponseDto closeGroup(Long groupId, String username) {
		return null;
	}
}
