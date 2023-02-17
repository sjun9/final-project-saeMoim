package com.saemoim.service;

import java.util.List;

import com.saemoim.dto.TagResponseDto;

public interface TagService {
	List<TagResponseDto> getTags();
}
