package com.saemoim.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
import com.saemoim.domain.enums.UserRoleEnum;
import com.saemoim.dto.request.CommentRequestDto;
import com.saemoim.dto.response.CommentResponseDto;
import com.saemoim.repository.CommentRepository;
import com.saemoim.repository.PostRepository;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

	@Mock
	private PostRepository postRepository;
	@Mock
	private CommentRepository commentRepository;
	@InjectMocks
	private CommentServiceImpl commentService;

	@Test
	@DisplayName("댓글 작성")
	public void createCommentTest() throws Exception{
		// 목적 : createComment() 정상동작 확인
		// 방법 : service.createComment() 실행한 결과값과 기대값 대조하여 확인
		// 실행한 결과값은 ? CommentResponseDto (id ,comment ,userId ,username ,createdAt ,modifiedAt)

		// given
		CommentRequestDto requestDto = CommentRequestDto.builder()
			.comment("댓글입니다.")
			.build();
		User user = new User("1234@naver.com", "1234", "오오륙", UserRoleEnum.USER);
		Post post = new Post();
		Comment comment = new Comment(post,"댓글 내용입니다.", user);

		when(commentRepository.save(comment)).thenReturn(comment);
		when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
		//when
		CommentResponseDto testComment = commentService.createComment(1L, requestDto, user);

		// then
	}
}

