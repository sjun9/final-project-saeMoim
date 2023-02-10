package com.saemoim.service;

import java.util.List;

import com.saemoim.dto.response.MyGroupResponseDto;
import com.saemoim.dto.response.StatusResponseDto;

public interface WishService {
	List<MyGroupResponseDto> getWishGroups(String username);

	StatusResponseDto wishGroup(Long groupId, String username);

	StatusResponseDto deleteWishGroup(Long groupId, String username);

}
