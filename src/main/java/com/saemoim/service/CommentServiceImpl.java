package com.saemoim.service;

import java.util.List;

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
import com.saemoim.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

	private final PostRepository postRepository;
	private final CommentRepository commentRepository;
	private final UserRepository userRepository;

	@Override
	@Transactional(readOnly = true)
	public List<CommentResponseDto> getComments(Long postId) {
		return commentRepository.findAllByPostIdOrderByCreatedAtDesc(postId)
			.stream()
			.map(CommentResponseDto::new)
			.toList();
	}

	@Override
	@Transactional
	public void createComment(Long postId, CommentRequestDto requestDto, Long userId) {
		Post post = postRepository.findById(postId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_POST.getMessage())
		);
		User user = userRepository.findById(userId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage())
		);

		commentRepository.save(new Comment(user, post, requestDto.getComment()));
	}

	@Transactional
	@Override
	public void updateComment(Long commentId, CommentRequestDto requestDto, Long userId) {
		Comment savedComment = _getCommentById(commentId);

		if (!savedComment.isWriter(userId)) {
			throw new IllegalArgumentException(ErrorCode.NOT_MATCH_USER.getMessage());
		}

		savedComment.update(requestDto.getComment());
		commentRepository.save(savedComment);
	}

	@Transactional
	@Override
	public void deleteComment(Long commentId, Long userId) {
		Comment comment = _getCommentById(commentId);

		if (!comment.isWriter(userId)) {
			throw new IllegalArgumentException(ErrorCode.NOT_MATCH_USER.getMessage());
		}

		commentRepository.delete(comment);
	}

	private Comment _getCommentById(Long commentId) {
		return commentRepository.findById(commentId)
			.orElseThrow(() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_COMMENT.getMessage()));
	}
}
