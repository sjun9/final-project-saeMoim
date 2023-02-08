package com.saemoim.service;

import java.util.List;

import com.saemoim.dto.request.GroupRequestDto;
import com.saemoim.dto.response.GroupResponseDto;
import com.saemoim.dto.response.MyGroupResponseDto;
import com.saemoim.dto.response.StatusResponseDto;

public class GroupServiceImpl implements GroupService {
	@Override
	public List<MyGroupResponseDto> getGroups() {
		return null;
	}

	@Override
	public MyGroupResponseDto getGroup(Long groupId) {
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
	public GroupResponseDto createGroup(GroupRequestDto requestDto, String username) {
		return null;
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
