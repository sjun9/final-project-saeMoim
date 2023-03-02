package com.saemoim.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.saemoim.domain.Category;
import com.saemoim.domain.Group;
import com.saemoim.domain.Participant;
import com.saemoim.domain.Tag;
import com.saemoim.domain.User;
import com.saemoim.domain.enums.GroupStatusEnum;
import com.saemoim.dto.request.GroupRequestDto;
import com.saemoim.dto.response.GroupResponseDto;
import com.saemoim.exception.ErrorCode;
import com.saemoim.fileUpload.AWSS3Uploader;
import com.saemoim.repository.CategoryRepository;
import com.saemoim.repository.GroupRepository;
import com.saemoim.repository.ParticipantRepository;
import com.saemoim.repository.TagRepository;
import com.saemoim.repository.UserRepository;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

	private final GroupRepository groupRepository;
	private final CategoryRepository categoryRepository;
	private final ParticipantRepository participantRepository;
	private final UserRepository userRepository;
	private final TagRepository tagRepository;
	private final AWSS3Uploader awss3Uploader;

	@Override
	@Transactional(readOnly = true)
	public Slice<GroupResponseDto> getAllGroups(Pageable pageable) {
		Slice<Group> groups = groupRepository.findAllByOrderByCreatedAtDesc(pageable);
		return groups.map(GroupResponseDto::new);
	}

	@Override
	@Transactional(readOnly = true)
	public GroupResponseDto getGroup(Long groupId) {
		Group group = _getGroupById(groupId);
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
		Slice<Group> groups;
		if (categoryId == 0L) {
			groups = groupRepository.findAllByOrderByCreatedAtDesc(pageable);
		} else {
			Category category = categoryRepository.findById(categoryId).orElseThrow(
				() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_CATEGORY.getMessage())
			);
			if (category.getParentId() == null) {
				throw new IllegalArgumentException(ErrorCode.NOT_CHILD_CATEGORY.getMessage());
			}
			if (status.equals(GroupStatusEnum.OPEN.toString())) {
				groups = groupRepository.findByCategoryAndStatusIsOpen(category, pageable);
			} else if (status.equals(GroupStatusEnum.CLOSE.toString())) {
				groups = groupRepository.findByCategoryAndStatusIsClose(category, pageable);
			} else {
				groups = groupRepository.findByCategory(category, pageable);
			}
		}

		return groups.map(GroupResponseDto::new);
	}

	@Override
	@Transactional(readOnly = true)
	public Slice<GroupResponseDto> searchGroupsByTag(String tagName, Pageable pageable) {
		Slice<Group> groups = tagRepository.findAllByNameContaining(tagName, pageable).map(Tag::getGroup);

		return groups.map(GroupResponseDto::new);
	}

	@Override
	@Transactional(readOnly = true)
	public Slice<GroupResponseDto> searchGroups(String groupName, Pageable pageable) {
		Slice<Group> groups = groupRepository.findAllByNameContainingOrderByCreatedAtDesc(groupName, pageable);
		return groups.map(GroupResponseDto::new);
	}

	@Override
	@Transactional(readOnly = true)
	public List<GroupResponseDto> getMyGroupsByLeader(Long userId) {
		return groupRepository.findAllByUser_IdOrderByCreatedAtDesc(userId)
			.stream().map(GroupResponseDto::new).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<GroupResponseDto> getMyGroupsByParticipant(Long userId) {
		return participantRepository.findAllByUser_IdOrderByCreatedAtDesc(userId)
			.stream()
			.map(Participant::getGroup)
			.map(GroupResponseDto::new).toList();
	}

	@Override
	@Transactional
	public GroupResponseDto createGroup(GroupRequestDto requestDto, Long userId, MultipartFile multipartFile) {
		User user = userRepository.findById(userId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage())
		);
		if (groupRepository.existsByName(requestDto.getName())) {
			throw new IllegalArgumentException(ErrorCode.DUPLICATED_GROUP_NAME.getMessage());
		}

		Category category = categoryRepository.findByName(requestDto.getCategoryName()).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_CATEGORY.getMessage())
		);
		if (category.getParentId() == null) {
			throw new IllegalArgumentException(ErrorCode.NOT_PARENT_CATEGORY.getMessage());
		}
		String dirName = "group";
		String imgPath;
		if(multipartFile == null){
			Group newGroup = new Group(requestDto, category, user);
			groupRepository.save(newGroup);

			return new GroupResponseDto(newGroup);
		}

		try {
			imgPath = awss3Uploader.upload(multipartFile, dirName);
		} catch (IOException e) {
			throw new IllegalArgumentException(ErrorCode.FAIL_IMAGE_UPLOAD.getMessage());
		}
		Group newGroup = new Group(requestDto, category, user, imgPath);
		groupRepository.save(newGroup);

		return new GroupResponseDto(newGroup);
	}

	@Override
	@Transactional
	public GroupResponseDto updateGroup(Long groupId, GroupRequestDto requestDto, Long userId) {
		Category category = categoryRepository.findByName(requestDto.getCategoryName()).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_CATEGORY.getMessage())
		);
		if (category.getParentId() == null) {
			throw new IllegalArgumentException(ErrorCode.NOT_PARENT_CATEGORY.getMessage());
		}
		Group group = _getGroupById(groupId);
		if (!group.isLeader(userId)) {
			throw new IllegalArgumentException(ErrorCode.INVALID_USER.getMessage());
		}

		group.update(requestDto, category, group.getUser());
		groupRepository.save(group);
		return new GroupResponseDto(group);
	}

	@Override
	@Transactional
	public void deleteGroup(Long groupId, Long userId) {
		Group group = _getGroupById(groupId);
		if (!group.isLeader(userId)) {
			throw new IllegalArgumentException(ErrorCode.INVALID_USER.getMessage());
		}
		groupRepository.delete(group);
	}

	@Override
	@Transactional
	public void openGroup(Long groupId, Long userId) {
		Group group = _getGroupById(groupId);
		if (group.isOpen()) {
			throw new IllegalArgumentException(ErrorCode.ALREADY_OPEN.getMessage());
		}
		if (!group.isLeader(userId)) {
			throw new IllegalArgumentException(ErrorCode.INVALID_USER.getMessage());
		}
		group.updateStatusToOpen();
	}

	@Transactional
	@Override
	public void closeGroup(Long groupId, Long userId) {
		Group group = _getGroupById(groupId);
		if (group.isClose()) {
			throw new IllegalArgumentException(ErrorCode.ALREADY_CLOSE.getMessage());
		}
		if (!group.isLeader(userId)) {
			throw new IllegalArgumentException(ErrorCode.INVALID_USER.getMessage());
		}
		group.updateStatusToClose();
	}

	private Group _getGroupById(Long groupId) {
		return groupRepository.findById(groupId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_GROUP.getMessage())
		);
	}
}
