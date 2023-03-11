package com.saemoim.service;

import java.util.List;

import com.saemoim.dto.request.CommentRequestDto;
import com.saemoim.dto.response.CommentResponseDto;

public interface CommentService {

	void createComment(Long postId, CommentRequestDto requestDto, Long userId);

	void updateComment(Long commentId, CommentRequestDto requestDto, Long userId);

	void deleteComment(Long commentId, Long userId);

	List<CommentResponseDto> getComments(Long postId);
}
