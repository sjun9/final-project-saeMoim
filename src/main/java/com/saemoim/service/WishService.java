package com.saemoim.service;

import java.util.List;

import com.saemoim.dto.response.MyGroupResponseDto;

public interface WishService {
	List<MyGroupResponseDto> getWishGroups(Long userId);

	void wishGroup(Long groupId, Long userId);

	void deleteWishGroup(Long groupId, Long userId);

}
