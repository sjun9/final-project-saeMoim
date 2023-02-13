package com.saemoim.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.saemoim.domain.User;
import com.saemoim.dto.request.GroupRequestDto;
import com.saemoim.dto.response.GroupResponseDto;
import com.saemoim.dto.response.MyGroupResponseDto;

public interface GroupService {
	Page<GroupResponseDto> getAllGroups(Pageable pageable);

	GroupResponseDto getGroup(Long groupId);

	List<MyGroupResponseDto> getMyGroupsByLeader(User user);

	List<MyGroupResponseDto> getMyGroupsByParticipant(User user);

	GroupResponseDto createGroup(GroupRequestDto requestDto, User user);

	GroupResponseDto updateGroup(Long groupId, GroupRequestDto requestDto, String username);

	void deleteGroup(Long groupId, String username);

	void openGroup(Long groupId, String username);

	void closeGroup(Long groupId, String username);

}
