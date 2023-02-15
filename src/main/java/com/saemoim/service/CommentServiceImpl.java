package com.saemoim.service;

import java.util.List;
import java.util.Objects;
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

	private final PostRepository postRepository;
	private final CommentRepository commentRepository;

	@Override
	@Transactional
	public CommentResponseDto createComment(Long postId, CommentRequestDto requestDto, Long userId) {
		// 게시글 유무 확인
		Post savedPost = postRepository.findById(postId).orElseThrow(()-> new IllegalArgumentException(ErrorCode.NOT_FOUND_POST.getMessage()));
		// 요청받은 댓글 내용 꺼내기
		String content = requestDto.getComment();
		// Comment savedComment = commentRepository.save(new Comment(savedPost, content, user));
		//return new CommentResponseDto(savedComment);
		return null;
	}

	@Transactional
	@Override
	public CommentResponseDto updateComment(Long commentId, CommentRequestDto requestDto, Long userId) {

		Comment savedComment = commentRepository.findById(commentId)
			.orElseThrow(() -> new IllegalArgumentException("임의 값 지정 수정 필요"));
		if (Objects.equals(userId, savedComment.getUser().getId())) {
			String comment = requestDto.getComment();
			savedComment.update(comment);
		}else {
			throw new IllegalArgumentException("임의 값 지정 수정 필요");
		}
		return new CommentResponseDto(savedComment);
	}

	@Transactional
	@Override
	public void deleteComment(Long commentId, Long userId) {

		Comment savedComment = commentRepository.findById(commentId)
			.orElseThrow(() -> new IllegalArgumentException("임의 값 지정 수정 필요"));

		if (userId.equals(savedComment.getUser().getId())) {
			commentRepository.delete(savedComment);
		}else {
			throw new IllegalArgumentException("임의 값 지정 수정 필요");
		}

	}

	@Override
	@Transactional(readOnly = true)
	public List<CommentResponseDto> findAllComment(Long postId) {
		return null;
	}
}
