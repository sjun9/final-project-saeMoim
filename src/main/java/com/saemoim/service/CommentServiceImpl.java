package com.saemoim.service;

import java.util.ArrayList;
import java.util.List;
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
import com.saemoim.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

	private final PostRepository postRepository;
	private final CommentRepository commentRepository;
	private final UserRepository userRepository;
	@Override
	@Transactional
	public CommentResponseDto createComment(Long postId, CommentRequestDto requestDto, Long userId) {
		Post savedPost = postRepository.findById(postId).orElseThrow(()-> new IllegalArgumentException(ErrorCode.NOT_FOUND_POST.getMessage()));
		User user = userRepository.findById(userId).orElseThrow(()-> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage()));
		String content = requestDto.getComment();
		Comment comment = new Comment(savedPost, content, user);
		Comment savedComment = commentRepository.save(comment);

		return new CommentResponseDto(savedComment);
	}

	@Transactional
	@Override
	public CommentResponseDto updateComment(Long commentId, CommentRequestDto requestDto, Long userId) {
		Comment savedComment = commentRepository.findById(commentId)
			.orElseThrow(() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_COMMENT.getMessage()));
		if (savedComment.getUser().getId().equals(userId)) {
			String comment = requestDto.getComment();
			savedComment.update(comment);

			return new CommentResponseDto(savedComment);
		}else {
			throw new IllegalArgumentException(ErrorCode.NOT_MATCH_USER.getMessage());
		}
	}

	@Transactional
	@Override
	public void deleteComment(Long commentId, Long userId) {
		Comment savedComment = commentRepository.findById(commentId)
			.orElseThrow(() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_COMMENT.getMessage()));

		if (userId.equals(savedComment.getUser().getId())) {
			commentRepository.delete(savedComment);
		}else {
			throw new IllegalArgumentException(ErrorCode.NOT_MATCH_USER.getMessage());
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<CommentResponseDto> getComments(Long postId) {
		List<Comment> comments = commentRepository.findAllByPostId(postId);
		List<CommentResponseDto> responseDtoList = new ArrayList<>();

		for (Comment comment : comments) {
			responseDtoList.add(new CommentResponseDto(comment));
		}

		return responseDtoList;
	}
}
