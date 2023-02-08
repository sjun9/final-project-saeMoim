package com.saemoim.service;

import com.saemoim.dto.request.CommentRequestDto;
import com.saemoim.dto.response.CommentResponseDto;
import com.saemoim.dto.response.StatusResponseDto;

public interface CommentService {

	CommentResponseDto createComment(Long postId, CommentRequestDto requestDto, String username);

	CommentResponseDto updateComment(Long commentId, CommentRequestDto requestDto, String username);

	StatusResponseDto deleteComment(Long commentId, String username);
}
