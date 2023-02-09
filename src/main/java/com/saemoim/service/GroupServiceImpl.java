package com.saemoim.service;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saemoim.domain.Category;
import com.saemoim.domain.Group;
import com.saemoim.domain.User;
import com.saemoim.dto.request.GroupRequestDto;
import com.saemoim.dto.response.GroupResponseDto;
import com.saemoim.dto.response.MyGroupResponseDto;
import com.saemoim.dto.response.StatusResponseDto;
import com.saemoim.exception.ErrorCode;
import com.saemoim.repository.CategoryRepository;
import com.saemoim.repository.GroupRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

	private final GroupRepository groupRepository;
	private final CategoryRepository categoryRepository;

	@Override
	public Page<GroupResponseDto> getGroups(Pageable pageable) {
		List<Group> groups = groupRepository.findAllByOrderByCreatedAtDesc(pageable);
		List<GroupResponseDto> groupResponseDto = new ArrayList<>();
		for (Group group : groups) {
			groupResponseDto.add(new GroupResponseDto(group));
		}
		return new PageImpl<>(groupResponseDto);
	}

	@Override
	public GroupResponseDto getGroup(Long groupId) {
		return null;
	}

	@Override
	public List<MyGroupResponseDto> getMyGroupsByLeader(String username) {
		return null;
	}

	@Override
	public List<MyGroupResponseDto> getMyGroupsByParticipant(String username) {
		return null;
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
		groupRepository.save(new Group(requestDto, category, user));
	}

	@Override
	public GroupResponseDto updateGroup(Long groupId, GroupRequestDto requestDto, String username) {
		return null;
	}

	@Override
	public StatusResponseDto deleteGroup(Long groupId, String username) {
		return null;
	}

	@Override
	public StatusResponseDto openGroup(Long groupId, String username) {
		return null;
	}

	@Override
	public StatusResponseDto closeGroup(Long groupId, String username) {
		return null;
	}

	@Override
	public List<MyGroupResponseDto> getWishGroups(String username) {
		return null;
	}

	@Override
	public StatusResponseDto wishGroup(Long groupId, String username) {
		return null;
	}

	@Override
	public StatusResponseDto deleteWishGroup(Long groupId, String username) {
		return null;
	}

	@Override
	public StatusResponseDto withdrawGroup(Long participantId, String username) {
		return null;
	}
}
