package com.saemoim.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.saemoim.domain.User;
import com.saemoim.dto.request.GroupRequestDto;
import com.saemoim.dto.response.GroupResponseDto;
import com.saemoim.dto.response.MyGroupResponseDto;
import com.saemoim.dto.response.StatusResponseDto;

public interface GroupService {
	Page<GroupResponseDto> getAllGroups(Pageable pageable);

	GroupResponseDto getGroup(Long groupId);

	List<MyGroupResponseDto> getMyGroupsByLeader(Long userId);

	List<MyGroupResponseDto> getMyGroupsByParticipant(Long userId);

	GroupResponseDto createGroup(GroupRequestDto requestDto, User user);

	GroupResponseDto updateGroup(Long groupId, GroupRequestDto requestDto, String username);

	StatusResponseDto deleteGroup(Long groupId, String username);

	StatusResponseDto openGroup(Long groupId, String username);

	StatusResponseDto closeGroup(Long groupId, String username);

}
