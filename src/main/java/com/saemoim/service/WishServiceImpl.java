package com.saemoim.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saemoim.dto.response.MyGroupResponseDto;
import com.saemoim.dto.response.StatusResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WishServiceImpl implements WishService {
	@Transactional
	@Override
	public List<MyGroupResponseDto> getWishGroups(String username) {
		return null;
	}

	@Transactional
	@Override
	public StatusResponseDto wishGroup(Long groupId, String username) {
		return null;
	}

	@Transactional
	@Override
	public StatusResponseDto deleteWishGroup(Long groupId, String username) {
		return null;
	}
}
