package com.saemoim.service;

import java.util.List;

import com.saemoim.dto.response.BlackListResponseDto;

public interface BlackListService {

	List<BlackListResponseDto> getBlacklists();

	void imposePermanentBan(Long blacklistId);

	void addBlacklist(Long userId);

	void deleteBlacklist(Long blacklistId);
}
