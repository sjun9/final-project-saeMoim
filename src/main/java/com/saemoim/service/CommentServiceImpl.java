package com.saemoim.service;

import com.saemoim.dto.request.CommentRequestDto;
import com.saemoim.dto.response.CommentResponseDto;
import com.saemoim.dto.response.StatusResponseDto;

public class CommentServiceImpl implements CommentService {
	@Override
	public CommentResponseDto createComment(Long postId, CommentRequestDto requestDto, String username) {
		return null;
	}

	@Override
	public CommentResponseDto updateComment(Long commentId, CommentRequestDto requestDto, String username) {
		return null;
	}

	@Override
	public StatusResponseDto deleteComment(Long commentId, String username) {
		return null;
	}
}
