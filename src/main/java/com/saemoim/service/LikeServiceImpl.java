package com.saemoim.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saemoim.domain.Post;
import com.saemoim.domain.PostLike;
import com.saemoim.exception.ErrorCode;
import com.saemoim.repository.LikeRepository;
import com.saemoim.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LikeServiceImpl implements LikeService {

	private final LikeRepository likeRepository;
	private final PostRepository postRepository;

	@Override
	@Transactional
	public String checkLike(Long postId, Long userId) {
		if (likeRepository.existsByPost_IdAndUserId(postId, userId)) {
			return "true";
		} else {
			return "false";
		}
	}

	@Override
	@Transactional
	public void addLike(Long postId, Long userId) {
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_POST.getMessage()));
		post.plusLikeCount();
		likeRepository.save(new PostLike(post, userId));

	}

	@Override
	@Transactional
	public void deleteLike(Long postId, Long userId) {
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_POST.getMessage()));
		post.minusLikeCount();
		likeRepository.deleteByPost_IdAndUserId(postId, userId);

	}
}
