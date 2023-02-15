package com.saemoim.service;

import java.util.List;

import com.saemoim.domain.User;
import com.saemoim.dto.request.CommentRequestDto;
import com.saemoim.dto.response.CommentResponseDto;

public interface CommentService {

	// CommentResponseDto createComment(Long postId, CommentRequestDto requestDto, String username);
	CommentResponseDto createComment(Long postId, CommentRequestDto requestDto, Long userId);

	CommentResponseDto updateComment(Long commentId, CommentRequestDto requestDto, String username);

	void deleteComment(Long commentId, String username);

	List<CommentResponseDto> findAllComment(Long postId);


}
