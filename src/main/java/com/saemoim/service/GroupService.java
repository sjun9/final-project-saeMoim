package com.saemoim.service;

import java.awt.print.Pageable;
import java.util.List;

import org.springframework.data.domain.Page;

import com.saemoim.domain.User;
import com.saemoim.dto.request.GroupRequestDto;
import com.saemoim.dto.response.GroupResponseDto;
import com.saemoim.dto.response.MyGroupResponseDto;
import com.saemoim.dto.response.StatusResponseDto;

public interface GroupService {
	Page<GroupResponseDto> getGroups(Pageable pageable);

	GroupResponseDto getGroup(Long groupId);

	List<MyGroupResponseDto> getMyGroupsByLeader(String username);

	List<MyGroupResponseDto> getMyGroupsByParticipant(String username);

	GroupResponseDto createGroup(GroupRequestDto requestDto, User user);

	GroupResponseDto updateGroup(Long groupId, GroupRequestDto requestDto, String username);

	StatusResponseDto deleteGroup(Long groupId, String username);

	StatusResponseDto openGroup(Long groupId, String username);

	StatusResponseDto closeGroup(Long groupId, String username);

	List<MyGroupResponseDto> getWishGroups(String username);

	StatusResponseDto wishGroup(Long groupId, String username);

	StatusResponseDto deleteWishGroup(Long groupId, String username);

	StatusResponseDto withdrawGroup(Long participantId, String username);

}
