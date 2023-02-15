package com.saemoim.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.saemoim.domain.Group;
import com.saemoim.domain.Review;
import com.saemoim.domain.User;
import com.saemoim.domain.enums.UserRoleEnum;
import com.saemoim.dto.request.ReviewRequestDto;
import com.saemoim.dto.response.ReviewResponseDto;
import com.saemoim.repository.GroupRepository;
import com.saemoim.repository.ReviewRepository;
import com.saemoim.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {
	@Mock
	private ReviewRepository reviewRepository;
	@Mock
	private GroupRepository groupRepository;
	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private ReviewServiceImpl reviewService;

	@Test
	@DisplayName("후기조회")
	void getReviews() {
		// given
		var groupId = 1L;
		var group = Group.builder().build();
		when(groupRepository.findById(anyLong())).thenReturn(Optional.of(group));
		// when
		reviewService.getReviews(groupId);
		// then
		verify(reviewRepository).findAllByGroupOrderByCreatedAtDesc(group);
	}

	@Test
	@DisplayName("후기작성")
	void createReview() {
		// given
		var groupId = 1L;
		var request = ReviewRequestDto.builder().content("내용").build();
		var username = "name";
		var group = Group.builder().build();
		var user = new User("e", "p", "name", UserRoleEnum.USER);

		when(groupRepository.findById(anyLong())).thenReturn(Optional.of(group));
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
		// when
		reviewService.createReview(groupId, request, username);
		// then
		verify(reviewRepository).save(any(Review.class));
	}

	@Test
	@DisplayName("후기수정")
	void updateReview() {
		// given
		Long id = 1L;
		ReviewRequestDto request = ReviewRequestDto.builder().content("내용").build();
		String username = "name";
		Review review = Review.builder()
			.user(new User("e", "p", "name", UserRoleEnum.USER))
			.content("아냐")
			.build();
		
		when(reviewRepository.findById(anyLong())).thenReturn(Optional.of(review));

		// when
		ReviewResponseDto response = reviewService.updateReview(id, request, username);
		// then
		assertThat(response.getContent()).isEqualTo("내용");
		verify(reviewRepository).save(review);
	}

	@Test
	@DisplayName("후기삭제")
	void deleteReview() {
		// given
		var id = 1L;
		var username = "name";
		var review = Review.builder().user(new User("e", "p", "name", UserRoleEnum.USER))
			.build();
		when(reviewRepository.findById(anyLong())).thenReturn(Optional.of(review));
		// when
		reviewService.deleteReview(id, username);
		// then
		verify(reviewRepository).delete(review);
	}
}