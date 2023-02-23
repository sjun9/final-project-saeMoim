package com.saemoim.service;

import com.saemoim.dto.response.MessageResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface LikeService {
	MessageResponseDto checkLike(Long postId, Long userId);

	void addLike(Long postId, Long userId);

	void deleteLike(Long postId, Long userId);
}
