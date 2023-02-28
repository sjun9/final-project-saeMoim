package com.saemoim.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.saemoim.dto.request.GroupRequestDto;
import com.saemoim.dto.response.GroupResponseDto;

public interface GroupService {
	Slice<GroupResponseDto> getAllGroups(Pageable pageable);

	GroupResponseDto getGroup(Long groupId);

	List<GroupResponseDto> getGroupByPopularity();

	Slice<GroupResponseDto> getGroupsByCategoryAndStatus(Long categoryId, String status, Pageable pageable);

	Slice<GroupResponseDto> searchGroupsByTag(String tagName, Pageable pageable);

	Slice<GroupResponseDto> searchGroups(String groupName, Pageable pageable);

	List<GroupResponseDto> getMyGroupsByLeader(Long userId);

	List<GroupResponseDto> getMyGroupsByParticipant(Long userId);

	GroupResponseDto createGroup(GroupRequestDto requestDto, Long userId);

	GroupResponseDto updateGroup(Long groupId, GroupRequestDto requestDto, Long userId);

	void deleteGroup(Long groupId, Long userId);

	void openGroup(Long groupId, Long userId);

	void closeGroup(Long groupId, Long userId);

}
