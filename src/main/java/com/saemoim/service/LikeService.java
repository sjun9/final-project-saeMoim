package com.saemoim.service;

import org.springframework.stereotype.Service;

@Service
public interface LikeService {
	boolean checkLike(Long postId, Long userId);

	void addLike(Long postId, Long userId);

	void deleteLike(Long postId, Long userId);
}
