package com.saemoim.service;

import java.util.List;

import com.saemoim.dto.response.GroupResponseDto;

public interface WishService {
	List<GroupResponseDto> getWishGroups(Long userId);

	void wishGroup(Long groupId, Long userId);

	void deleteWishGroup(Long groupId, Long userId);

}
