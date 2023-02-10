package com.saemoim.service;

import java.util.List;

import com.saemoim.dto.request.GroupRequestDto;
import com.saemoim.dto.response.GroupResponseDto;
import com.saemoim.dto.response.MyGroupResponseDto;
import com.saemoim.dto.response.StatusResponseDto;

public interface GroupService {
	List<MyGroupResponseDto> getGroups();

	MyGroupResponseDto getGroup(Long groupId);

	List<MyGroupResponseDto> getMyGroupsByLeader(String username);

	List<MyGroupResponseDto> getMyGroupsByParticipant(String username);

	GroupResponseDto createGroup(GroupRequestDto requestDto, String username);

	GroupResponseDto updateGroup(Long groupId, GroupRequestDto requestDto, String username);

	StatusResponseDto deleteGroup(Long groupId, String username);

	StatusResponseDto openGroup(Long groupId, String username);

	StatusResponseDto closeGroup(Long groupId, String username);

}
