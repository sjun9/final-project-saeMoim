package com.saemoim.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saemoim.domain.Event;
import com.saemoim.dto.request.EventRequestDto;
import com.saemoim.dto.response.EventResponseDto;
import com.saemoim.exception.ErrorCode;
import com.saemoim.repository.EventRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService{
	private final EventRepository eventRepository;

	@Override
	@Transactional(readOnly = true)
	public List<EventResponseDto> getEvents() {
		return eventRepository.findAllByFinishedIsFalseOrderByCreatedAtDesc()
			.stream().map(EventResponseDto::new).toList();
	}

	@Override
	@Transactional
	public void createEvent(EventRequestDto requestDto) {
		if (eventRepository.existsByName(requestDto.getName())) {
			throw new IllegalArgumentException(ErrorCode.DUPLICATED_EVENT.getMessage());
		}

		Event event = Event.builder()
			.name(requestDto.getName())
			.content(requestDto.getContent())
			.startTime(requestDto.getStartTime())
			.quantity(requestDto.getQuantity())
			.finished(requestDto.isFinished())
			.build();

		eventRepository.save(event);
	}

	@Override
	@Transactional
	public void updateEvent(Long eventId, EventRequestDto requestDto) {
		Event event = _getEventById(eventId);

		if (eventRepository.existsByName(requestDto.getName())) {
			new IllegalArgumentException(ErrorCode.DUPLICATED_EVENT.getMessage());
		}

		event.updateEvent(requestDto);
		eventRepository.save(event);
	}

	@Override
	@Transactional
	public void finishEvent(Long eventId) {
		Event event = _getEventById(eventId);

		event.finishEvent();
		eventRepository.save(event);
	}

	private Event _getEventById(Long eventId) {
		return eventRepository.findById(eventId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_EVENT.getMessage())
		);
	}
}
