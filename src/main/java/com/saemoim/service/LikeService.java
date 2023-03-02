package com.saemoim.service;

import org.springframework.stereotype.Service;

@Service
public interface LikeService {
	void addLike(Long postId, Long userId);

	void deleteLike(Long postId, Long userId);
}
