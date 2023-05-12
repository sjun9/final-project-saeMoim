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
import com.saemoim.dto.request.EventRequestDto;
import com.saemoim.dto.response.EventResponseDto;
import com.saemoim.repository.AdminRepository;
import com.saemoim.repository.EventRepository;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {
	@InjectMocks
	private EventServiceImpl eventService;
	@Mock
	private EventRepository eventRepository;
	@Mock
	private AdminRepository adminRepository;

	@Test
	@DisplayName("이벤트 목록 조회")
	void getEvents() {
		//given
		List<Event> events = new ArrayList<>();
		when(eventRepository.findAllByFinishedIsFalseOrderByCreatedAtDesc()).thenReturn(events);
		//when
		List<EventResponseDto> response = eventService.getEvents();
		//then
		verify(eventRepository).findAllByFinishedIsFalseOrderByCreatedAtDesc();
	}

	@Test
	void createEvent() {
		//given
		Long adminId = 1L;
		EventRequestDto requestDto = mock(EventRequestDto.class);
		Admin admin = mock(Admin.class);
		when(adminRepository.findById(adminId)).thenReturn(Optional.of(admin));
		when(requestDto.getName()).thenReturn("event");
		when(requestDto.getContent()).thenReturn("content");
		when(requestDto.getStartTime()).thenReturn(LocalDateTime.of(2023, 05, 12, 17, 40, 0));
		when(requestDto.getEndTime()).thenReturn(LocalDateTime.of(2023, 05, 12, 17, 50, 0));
		when(requestDto.getQuantity()).thenReturn(5);
		when(requestDto.isFinished()).thenReturn(false);
		when(eventRepository.existsByName(requestDto.getName())).thenReturn(false);
		//when
		eventService.createEvent(requestDto, adminId);
		//then
		verify(eventRepository).save(any(Event.class));
	}

	@Test
	void updateEvent() {
		//given
		Long eventId = 1L;
		EventRequestDto requestDto = mock(EventRequestDto.class);
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

		when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
		when(requestDto.getName()).thenReturn("event");
		when(requestDto.getContent()).thenReturn("content2");
		when(requestDto.getStartTime()).thenReturn(LocalDateTime.of(2023, 05, 12, 17, 40, 0));
		when(requestDto.getEndTime()).thenReturn(LocalDateTime.of(2023, 05, 12, 17, 50, 0));
		when(requestDto.getQuantity()).thenReturn(10);
		when(requestDto.isFinished()).thenReturn(false);
		when(eventRepository.existsByName(requestDto.getName())).thenReturn(false);
		//when
		eventService.updateEvent(eventId, requestDto);
		//then
		assertThat(event.getContent()).isEqualTo("content2");
		assertThat(event.getQuantity()).isEqualTo(10);
	}

	@Test
	void finishEvent() {
		//given
		Long eventId = 1L;
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
		when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
		//when
		eventService.finishEvent(eventId);
		//then
		assertThat(event.isFinished()).isTrue();
	}
}