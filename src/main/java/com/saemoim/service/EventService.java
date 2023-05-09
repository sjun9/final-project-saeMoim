package com.saemoim.service;

import java.util.List;

import com.saemoim.dto.request.EventRequestDto;
import com.saemoim.dto.response.EventResponseDto;

public interface EventService {
	List<EventResponseDto> getEvents();

	void createEvent(EventRequestDto requestDto);

	void updateEvent(Long eventId, EventRequestDto requestDto);

	void finishEvent(Long eventId);
}
