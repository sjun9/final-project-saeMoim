package com.saemoim.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.saemoim.domain.Post;
import com.saemoim.domain.PostLike;
import com.saemoim.repository.LikeRepository;
import com.saemoim.repository.PostRepository;

@ExtendWith(MockitoExtension.class)
class LikeServiceImplTest {
	@Mock
	private PostRepository postRepository;
	@Mock
	private LikeRepository likeRepository;
	@InjectMocks
	private LikeServiceImpl likeService;

	@Test
	@DisplayName("좋아요 누르기")
	void addLike() {
		//given
		Long postId = 1L;
		Long userId = 1L;
		Post post = mock(Post.class);
		when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
		//when
		likeService.addLike(postId, userId);
		//then
		verify(likeRepository).save(any(PostLike.class));
	}

	@Test
	@DisplayName("좋아요 취소")
	void deleteLike() {
		//given
		Long postId = 1L;
		Long userId = 1L;
		Post post = mock(Post.class);
		when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
		//when
		likeService.deleteLike(postId, userId);
		//then
		verify(likeRepository).deleteByPost_IdAndUserId(postId, userId);
	}
}