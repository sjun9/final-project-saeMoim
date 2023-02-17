package com.saemoim.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.saemoim.dto.request.GroupRequestDto;
import com.saemoim.dto.response.GroupResponseDto;

public interface GroupService {
	Slice<GroupResponseDto> getAllGroups(Pageable pageable);

	GroupResponseDto getGroup(Long groupId);

	GroupResponseDto createGroup(GroupRequestDto requestDto, Long userId);

	GroupResponseDto updateGroup(Long groupId, GroupRequestDto requestDto, String username);

	void deleteGroup(Long groupId, String username);

	void openGroup(Long groupId, String username);

	void closeGroup(Long groupId, String username);

}
