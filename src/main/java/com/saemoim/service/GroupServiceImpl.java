package com.saemoim.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saemoim.domain.Category;
import com.saemoim.domain.Group;
import com.saemoim.domain.Participant;
import com.saemoim.domain.Tag;
import com.saemoim.domain.User;
import com.saemoim.domain.enums.GroupStatusEnum;
import com.saemoim.dto.request.GroupRequestDto;
import com.saemoim.dto.response.GroupResponseDto;
import com.saemoim.exception.ErrorCode;
import com.saemoim.repository.CategoryRepository;
import com.saemoim.repository.GroupRepository;
import com.saemoim.repository.ParticipantRepository;
import com.saemoim.repository.TagRepository;
import com.saemoim.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

	private final GroupRepository groupRepository;
	private final CategoryRepository categoryRepository;
	private final ParticipantRepository participantRepository; // 원투매니
	private final UserRepository userRepository;
	private final TagRepository tagRepository;

	@Override
	@Transactional(readOnly = true)
	public Slice<GroupResponseDto> getAllGroups(Pageable pageable) {
		List<Group> groups = groupRepository.findAllByOrderByCreatedAtDesc(pageable);
		boolean hasNext = false;
		if (groups.size() > pageable.getPageSize()) {
			groups.remove(pageable.getPageSize());
			hasNext = true;
		}
		return new SliceImpl<>(groups.stream().map(GroupResponseDto::new).toList(), pageable, hasNext);
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
	@Cacheable(value = "popularGroup")
	public List<GroupResponseDto> getGroupByPopularity() {
		List<Group> groups = groupRepository.findAll();
		PriorityQueue<GroupResponseDto> queue = new PriorityQueue<>();
		for (Group group : groups) {
			if (queue.size() > 3) {
				if (queue.peek().getWishCount() < group.getWishCount()) {
					queue.poll();
					queue.add(new GroupResponseDto(group));
				}
			} else {
				queue.add(new GroupResponseDto(group));
			}
		}
		List<GroupResponseDto> list = new ArrayList<>(queue);
		list.sort(Collections.reverseOrder());
		return list;
	}

	@Override
	@Transactional(readOnly = true)
	public Slice<GroupResponseDto> getGroupsByCategoryAndStatus(Long categoryId, String status,
		Pageable pageable) {
		List<Group> groups;
		if (categoryId.equals(0L)) {
			groups = groupRepository.findAllByOrderByCreatedAtDesc(pageable);
		} else {
			Category category = categoryRepository.findById(categoryId).orElseThrow(
				() -> new IllegalArgumentException(ErrorCode.NOT_EXIST_CATEGORY.getMessage())
			);
			if (category.getParentId() == null) {
				throw new IllegalArgumentException(ErrorCode.NOT_CHILD_CATEGORY.getMessage()); // 에러메세지 다른걸로
			}
			groups = groupRepository.findAllByCategoryOrderByCreatedAtDesc(category);
		}

		if (status.equals(GroupStatusEnum.OPEN.toString())) {
			groups = groups.stream()
				.filter(g -> g.getStatus().equals(GroupStatusEnum.OPEN))
				.toList();
		} else if (status.equals(GroupStatusEnum.CLOSE.toString())) {
			groups = groups.stream()
				.filter(g -> g.getStatus().equals(GroupStatusEnum.CLOSE))
				.toList();
		}

		boolean hasNext = false;
		if (groups.size() > pageable.getPageSize()) {
			groups.remove(pageable.getPageSize());
			hasNext = true;
		}
		return new SliceImpl<>(groups.stream().map(GroupResponseDto::new).toList(), pageable, hasNext);
	}

	@Override
	@Transactional(readOnly = true)
	public Slice<GroupResponseDto> getGroupsByTag(String tagName, Pageable pageable) {
		List<Tag> tags = tagRepository.findAllByName(tagName);
		List<Group> groups = new ArrayList<>(tags.stream().map(Tag::getGroup).toList());
		boolean hasNext = false;
		if (groups.size() > pageable.getPageSize()) {
			groups.remove(pageable.getPageSize());
			hasNext = true;
		}
		return new SliceImpl<>(groups.stream().map(GroupResponseDto::new).toList(), pageable, hasNext);

	}

	@Override
	@Transactional(readOnly = true)
	public Slice<GroupResponseDto> searchGroups(String groupName, Pageable pageable) {
		List<Group> groups = groupRepository.findAllByNameContainingOrderByCreatedAtDesc(groupName);
		boolean hasNext = false;
		if (groups.size() > pageable.getPageSize()) {
			groups.remove(pageable.getPageSize());
			hasNext = true;
		}
		return new SliceImpl<>(groups.stream().map(GroupResponseDto::new).toList(), pageable, hasNext);
	}

	@Override
	@Transactional(readOnly = true)
	public List<GroupResponseDto> getMyGroupsByLeader(Long userId) {
		List<Group> groups = groupRepository.findAllByUser_IdOrderByCreatedAtDesc(userId);

		return groups.stream().map(GroupResponseDto::new).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<GroupResponseDto> getMyGroupsByParticipant(Long userId) {
		List<Group> groups = participantRepository.findAllByUser_IdOrderByCreatedAtDesc(userId)
			.stream()
			.map(Participant::getGroup)
			.toList();

		return groups.stream().map(GroupResponseDto::new).toList();
	}

	@Override
	@Transactional
	public GroupResponseDto createGroup(GroupRequestDto requestDto, Long userId) {
		User user = userRepository.findById(userId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage())
		);
		if (groupRepository.existsByName(requestDto.getName())) {
			throw new IllegalArgumentException(ErrorCode.DUPLICATED_GROUP_NAME.getMessage());
		}
		// 카테고리 존재 확인
		Category category = categoryRepository.findByName(requestDto.getCategoryName()).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_EXIST_CATEGORY.getMessage())
		);
		if (category.getParentId() == null) {
			throw new IllegalArgumentException(ErrorCode.NOT_PARENT_CATEGORY.getMessage());
		}
		Group newGroup = new Group(requestDto, category, user);
		groupRepository.save(newGroup);

		return new GroupResponseDto(newGroup);
	}

	@Override
	@Transactional
	public GroupResponseDto updateGroup(Long groupId, GroupRequestDto requestDto, String username) {
		// 카테고리 존재 확인
		Category category = categoryRepository.findByName(requestDto.getCategoryName()).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_EXIST_CATEGORY.getMessage())
		);
		if (category.getParentId() == null) {
			throw new IllegalArgumentException(ErrorCode.NOT_PARENT_CATEGORY.getMessage());
		}
		Group group = groupRepository.findById(groupId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_GROUP.getMessage())
		);
		if (group.isLeader(username)) {
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
		if (group.isLeader(username)) {
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
		if (group.isLeader(username))
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
		if (group.isLeader(username))
			group.updateStatusToClose();
		else {
			throw new IllegalArgumentException(ErrorCode.INVALID_USER.getMessage());
		}
	}
}
