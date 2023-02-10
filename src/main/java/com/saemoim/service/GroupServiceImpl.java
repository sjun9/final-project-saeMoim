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
import com.saemoim.domain.Review;
import com.saemoim.domain.Tag;
import com.saemoim.domain.User;
import com.saemoim.dto.request.GroupRequestDto;
import com.saemoim.dto.response.GroupResponseDto;
import com.saemoim.dto.response.MyGroupResponseDto;
import com.saemoim.dto.response.StatusResponseDto;
import com.saemoim.exception.ErrorCode;
import com.saemoim.repository.CategoryRepository;
import com.saemoim.repository.GroupRepository;
import com.saemoim.repository.ReviewRepository;
import com.saemoim.repository.TagRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

	private final GroupRepository groupRepository;
	private final CategoryRepository categoryRepository;
	private final ReviewRepository reviewRepository;
	private final TagRepository tagRepository;

	@Override
	@Transactional(readOnly = true)
	public Page<GroupResponseDto> getAllGroups(Pageable pageable) {
		List<Group> groups = groupRepository.findAllByOrderByCreatedAtDesc(pageable);
		List<GroupResponseDto> groupResponseDto = new ArrayList<>();
		for (Group group : groups) {
			List<Review> reviewList = reviewRepository.findAllByGroupIdOrderByCreatedAt(group.getId());
			List<String> tagList = tagRepository.findAllByGroupId(group.getId()).stream().map(Tag::getName).toList();
			// 태그의 이름만 잇는 리스트를 그룹 dto에 저장하려고

			groupResponseDto.add(new GroupResponseDto(group, reviewList, tagList));
		}
		return new PageImpl<>(groupResponseDto);
	}

	@Override
	@Transactional
	public GroupResponseDto getGroup(Long groupId) {
		Group group = groupRepository.findById(groupId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_GROUP.getMessage())
		);
		List<Review> reviewList = reviewRepository.findAllByGroupIdOrderByCreatedAt(groupId);
		List<String> tagList = tagRepository.findAllByGroupId(group.getId()).stream().map(Tag::getName).toList();
		return new GroupResponseDto(group, reviewList, tagList);
	}

	@Transactional(readOnly = true)
	@Override
	public List<MyGroupResponseDto> getMyGroupsByLeader(String username) {
		return null;
	}

	@Transactional(readOnly = true)
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
		Group group = new Group(requestDto, category, user);
		groupRepository.save(group);

		// 태그 생성 -> 태그서비스로 옮기고 여기서 태그서비스를 호출해서 사용하는게 좋지 않을까...원투매니..?
		List<String> tags = requestDto.getTagNames();
		for (String tag : tags) {
			tagRepository.save(new Tag(tag, group));
		}
		return new GroupResponseDto(group, new ArrayList<>(), tags);
	}

	@Transactional
	@Override
	public GroupResponseDto updateGroup(Long groupId, GroupRequestDto requestDto, String username) {
		// 그룹을 가져오기
		// 태그 가져오기
		// 그룹 수정
		// 태그 삭제
		// 태그 추가

	}

	@Transactional
	@Override
	public StatusResponseDto deleteGroup(Long groupId, String username) {
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
