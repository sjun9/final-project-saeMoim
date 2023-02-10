package com.saemoim.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saemoim.dto.response.BlackListResponseDto;
import com.saemoim.dto.response.StatusResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BlackListServiceImpl implements BlackListService {

	@Transactional(readOnly = true)
	@Override
	public List<BlackListResponseDto> getBlacklists() {
		return null;
	}

	@Transactional
	@Override
	public StatusResponseDto addBlacklist(Long userId) {
		return null;
	}

	@Transactional
	@Override
	public StatusResponseDto deleteBlacklist(Long userId) {
		return null;
	}
}
