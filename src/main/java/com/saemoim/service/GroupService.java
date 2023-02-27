package com.saemoim.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

import com.saemoim.dto.request.GroupRequestDto;
import com.saemoim.dto.response.GroupResponseDto;

public interface GroupService {
	Slice<GroupResponseDto> getAllGroups(Pageable pageable);

	GroupResponseDto getGroup(Long groupId);

	@Transactional(readOnly = true)
	@Cacheable(value = "popularGroup")
	List<GroupResponseDto> getGroupByPopularity();

	@Transactional(readOnly = true)
	Slice<GroupResponseDto> getGroupsByCategoryAndStatus(Long categoryId, String status,
		Pageable pageable);

	@Transactional(readOnly = true)
	Slice<GroupResponseDto> getGroupsByTag(String tagName, Pageable pageable);

	@Transactional(readOnly = true)
	Slice<GroupResponseDto> searchGroups(String groupName, Pageable pageable);

	@Transactional(readOnly = true)
	List<GroupResponseDto> getMyGroupsByLeader(Long userId);

	@Transactional(readOnly = true)
	List<GroupResponseDto> getMyGroupsByParticipant(Long userId);

	GroupResponseDto createGroup(GroupRequestDto requestDto, Long userId);

	GroupResponseDto updateGroup(Long groupId, GroupRequestDto requestDto, Long userId);

	void deleteGroup(Long groupId, Long userId);

	void openGroup(Long groupId, Long userId);

	void closeGroup(Long groupId, Long userId);

}
