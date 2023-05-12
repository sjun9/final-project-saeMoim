package com.saemoim.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.saemoim.domain.Admin;
import com.saemoim.domain.Event;
import com.saemoim.domain.Gift;
import com.saemoim.domain.User;
import com.saemoim.dto.response.GiftAdminResponseDto;
import com.saemoim.dto.response.GiftUserResponseDto;
import com.saemoim.repository.EventRepository;
import com.saemoim.repository.GiftRepository;
import com.saemoim.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class GiftServiceImplTest {
	@InjectMocks
	private GiftServiceImpl giftService;
	@Mock
	private GiftRepository giftRepository;
	@Mock
	private EventRepository eventRepository;
	@Mock
	private UserRepository userRepository;

	@Test
	@DisplayName("본인 당첨 내역 확인")
	void getGifts() {
		//given
		Long userId = 1L;
		List<Gift> gifts = new ArrayList<>();
		when(giftRepository.findByUser_IdOrderByCreatedAtDesc(userId)).thenReturn(gifts);
		//when
		List<GiftUserResponseDto> response = giftService.getGifts(userId);
		//then
		verify(giftRepository).findByUser_IdOrderByCreatedAtDesc(userId);
	}

	@Test
	@DisplayName("관리자 이벤트 당첨자 목록 확인")
	void getGiftsByEventId() {
		//given
		Long eventId = 1L;
		List<Gift> gifts = new ArrayList<>();
		when(giftRepository.findByEvent_IdOrderByCreatedAt(eventId)).thenReturn(gifts);
		//when
		List<GiftAdminResponseDto> response = giftService.getGiftsByEventId(eventId);
		//then
		verify(giftRepository).findByEvent_IdOrderByCreatedAt(eventId);
	}

	@Test
	@DisplayName("이벤트 신청하기")
	void applyEvent() {
		//given
		Long eventId = 1L;
		Long userId = 1L;
		Admin admin = mock(Admin.class);
		Event event = Event.builder()
			.id(eventId)
			.admin(admin)
			.name("event")
			.content("content")
			.startTime(LocalDateTime.of(2023, 05, 12, 17, 40, 0))
			.endTime(LocalDateTime.of(2023, 05, 12, 17, 50, 0))
			.quantity(1)
			.finished(false)
			.build();
		User user = mock(User.class);

		when(eventRepository.findByIdWithPessimisticLock(eventId)).thenReturn(Optional.of(event));
		when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		when(giftRepository.existsByEvent_IdAndUser_Id(eventId, userId)).thenReturn(false);
		//when
		giftService.applyEvent(eventId, userId);
		//then
		assertThat(event.getQuantity()).isEqualTo(0);
		assertThat(event.isFinished()).isTrue();
		verify(giftRepository).save(any(Gift.class));
	}

	@Test
	@DisplayName("선물 발송완료 상태 변경")
	void updateSend() {
		//given
		Long giftId = 1L;
		Event event = mock(Event.class);
		User user = mock(User.class);
		Gift gift = new Gift(event, user);
		when(giftRepository.findById(giftId)).thenReturn(Optional.of(gift));
		//when
		giftService.updateSend(giftId);
		//then
		assertThat(gift.isSend()).isTrue();
		verify(giftRepository).save(gift);
	}
}