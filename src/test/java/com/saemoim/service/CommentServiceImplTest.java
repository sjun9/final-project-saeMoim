package com.saemoim.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.saemoim.domain.Comment;
import com.saemoim.domain.Post;
import com.saemoim.domain.User;
import com.saemoim.dto.request.CommentRequestDto;
import com.saemoim.repository.CommentRepository;
import com.saemoim.repository.PostRepository;
import com.saemoim.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

	@Mock
	private PostRepository postRepository;
	@Mock
	private UserRepository userRepository;
	@Mock
	private CommentRepository commentRepository;
	@InjectMocks
	private CommentServiceImpl commentService;

	@Test
	@DisplayName("댓글 조회")
	public void getComments() {
		//given
		Long postId = 1L;
		List<Comment> list = new ArrayList<>();
		when(commentRepository.findAllByPostIdOrderByCreatedAtDesc(anyLong())).thenReturn(list);
		//when
		commentService.getComments(postId);
		//then
		verify(commentRepository).findAllByPostIdOrderByCreatedAtDesc(postId);
	}

	@Test
	@DisplayName("댓글 작성")
	public void createCommentTest() {
		// given
		CommentRequestDto requestDto = CommentRequestDto.builder()
			.comment("댓글입니다.")
			.build();
		User user = mock(User.class);
		Post post = new Post();
		Comment comment = new Comment(user, post, "댓글 내용입니다.");

		when(user.getId()).thenReturn(1L);
		when(commentRepository.save(any(Comment.class))).thenReturn(comment);
		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
		when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
		//when
		commentService.createComment(1L, requestDto, user.getId());
		// then
		verify(commentRepository).save(any(Comment.class));
	}

	@Test
	@DisplayName("댓글 수정")
	public void updateComment() {
		//given
		Long commentId = 1L;
		Long userId = 1L;
		CommentRequestDto requestDto = mock(CommentRequestDto.class);
		Comment comment = mock(Comment.class);

		when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));
		when(comment.isWriter(anyLong())).thenReturn(true);
		//when
		commentService.updateComment(commentId, requestDto, userId);
		//then
		verify(commentRepository).save(comment);
	}

	@Test
	@DisplayName("댓글 삭제")
	public void deleteComment() {
		//given
		Long commentId = 1L;
		Long userId = 1L;
		Comment comment = mock(Comment.class);

		when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));
		when(comment.isWriter(anyLong())).thenReturn(true);
		//when
		commentService.deleteComment(commentId, userId);
		//then
		verify(commentRepository).delete(comment);
	}
}

