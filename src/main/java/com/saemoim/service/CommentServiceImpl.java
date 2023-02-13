package com.saemoim.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saemoim.domain.Comment;
import com.saemoim.domain.Post;
import com.saemoim.domain.User;
import com.saemoim.dto.request.CommentRequestDto;
import com.saemoim.dto.response.CommentResponseDto;
import com.saemoim.exception.ErrorCode;
import com.saemoim.repository.CommentRepository;
import com.saemoim.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
	// @Transactional
	// @Override
	// public CommentResponseDto createComment(Long postId, CommentRequestDto requestDto, String username) {
	// 	return null;
	// }
	private final PostRepository postRepository;
	private final CommentRepository commentRepository;

	@Override
	@Transactional
	public CommentResponseDto createComment(Long postId, CommentRequestDto requestDto, User user) {
		Post savedPost = postRepository.findById(postId).orElseThrow(()-> new IllegalArgumentException(ErrorCode.NOT_FOUND_POST.getMessage()));
		String content = requestDto.getComment();
		Comment comment = new Comment(savedPost, content, user);
		commentRepository.save(comment);
		return null;
	}

	@Transactional
	@Override
	public CommentResponseDto updateComment(Long commentId, CommentRequestDto requestDto, String username) {
		return null;
	}

	@Transactional
	@Override
	public void deleteComment(Long commentId, String username) {

	}
}
