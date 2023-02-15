package com.saemoim.service;

import java.util.List;

import com.saemoim.dto.request.CommentRequestDto;
import com.saemoim.dto.response.CommentResponseDto;

public interface CommentService {

	CommentResponseDto createComment(Long postId, CommentRequestDto requestDto, Long userId);

	CommentResponseDto updateComment(Long commentId, CommentRequestDto requestDto, Long userId);

	void deleteComment(Long commentId, Long userId);

	List<CommentResponseDto> findAllComment(Long postId);

}
