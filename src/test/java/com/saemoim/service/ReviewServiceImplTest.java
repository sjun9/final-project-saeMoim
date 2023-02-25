package com.saemoim.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
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

import com.saemoim.domain.Group;
import com.saemoim.domain.Participant;
import com.saemoim.domain.Review;
import com.saemoim.domain.User;
import com.saemoim.domain.enums.UserRoleEnum;
import com.saemoim.dto.request.ReviewRequestDto;
import com.saemoim.dto.response.ReviewResponseDto;
import com.saemoim.repository.ParticipantRepository;
import com.saemoim.repository.ReviewRepository;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {
	@Mock
	private ReviewRepository reviewRepository;
	@Mock
	private ParticipantRepository participantRepository;
	@InjectMocks
	private ReviewServiceImpl reviewService;

	@Test
	@DisplayName("후기조회")
	void getReviews() {
		// given
		var groupId = 1L;
		// when
		reviewService.getReviews(groupId);
		// then
		verify(reviewRepository).findAllByGroup_IdOrderByCreatedAtDesc(groupId);
	}

	@Test
	@DisplayName("후기작성")
	void createReview() {
		// given
		var groupId = 1L;
		var request = ReviewRequestDto.builder().content("내용").build();
		var userId = 1L;
		var participant = mock(Participant.class);
		var user = mock(User.class);
		var group = mock(Group.class);

		when(participant.getGroup()).thenReturn(group);
		when(participant.getUser()).thenReturn(user);
		when(participantRepository.findByGroup_IdAndUser_Id(anyLong(), anyLong())).thenReturn(Optional.of(participant));
		// when
		reviewService.createReview(groupId, request, userId);
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

	@Test
	@DisplayName("관리자 후기삭제")
	void deleteReviewByAdmin() {
		// given
		Review review = mock(Review.class);

		when(reviewRepository.findById(anyLong())).thenReturn(Optional.of(review));
		// when
		reviewService.deleteReviewByAdmin(anyLong());
		// then
		verify(reviewRepository).delete(review);
	}
}