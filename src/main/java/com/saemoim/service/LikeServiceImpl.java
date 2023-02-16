package com.saemoim.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saemoim.domain.Post;
import com.saemoim.domain.PostLike;
import com.saemoim.exception.ErrorCode;
import com.saemoim.repository.LikeRepository;
import com.saemoim.repository.PostRepository;
import com.saemoim.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LikeServiceImpl implements LikeService{

	private final LikeRepository likeRepository;
	private final UserRepository userRepository;
	private final PostRepository postRepository;
	@Override
	@Transactional
	public void addLike(Long postId, Long userId) {

		if (likeRepository.existsByPost_IdAndUserId(postId, userId)) {
			throw new IllegalArgumentException(ErrorCode.DUPLICATED_LIKE.getMessage());
		} else {
			Post post = postRepository.findById(postId)
				.orElseThrow(() -> new IllegalArgumentException(ErrorCode.NOT_EXIST_POST.getMessage()));
			post.plusLikeCount();
			likeRepository.save(new PostLike(post, userId));
		}
	}

	@Override
	@Transactional
	public void deleteLike(Long postId, Long userId) {
		if (likeRepository.existsByPost_IdAndUserId(postId, userId)) {
			Post post = postRepository.findById(postId)
				.orElseThrow(() -> new IllegalArgumentException(ErrorCode.NOT_EXIST_POST.getMessage()));
			post.minusLikeCount();
			likeRepository.deleteByPost_IdAndUserId(postId, userId);
		}else {
			throw new IllegalArgumentException(ErrorCode.ALREADY_DELETED.getMessage());
		}
	}
}
