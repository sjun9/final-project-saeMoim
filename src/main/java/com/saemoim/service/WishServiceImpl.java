package com.saemoim.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saemoim.dto.response.MyGroupResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WishServiceImpl implements WishService {
	@Transactional
	@Override
	public List<MyGroupResponseDto> getWishGroups(Long userId) {
		return null;
	}

	@Transactional
	@Override
	public void wishGroup(Long groupId, Long userId) {

	}

	@Transactional
	@Override
	public void deleteWishGroup(Long groupId, Long userId) {

	}
}
