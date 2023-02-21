package com.saemoim.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saemoim.domain.Tag;
import com.saemoim.dto.response.TagResponseDto;
import com.saemoim.repository.TagRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

	private final TagRepository tagRepository;

	@Transactional(readOnly = true)
	@Override
	public List<TagResponseDto> getTags() {
		Set<String> tags = tagRepository.findAll().stream().map(Tag::getName).collect(Collectors.toSet());
		return tags.stream().map(TagResponseDto::new).toList();
	}
}
