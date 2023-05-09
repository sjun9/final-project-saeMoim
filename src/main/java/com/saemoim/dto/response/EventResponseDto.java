package com.saemoim.dto.response;

import java.time.LocalDateTime;

import com.saemoim.domain.Event;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventResponseDto {
	private Long id;
	private String name;
	private String content;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private int quantity;
	private boolean finished;

	public EventResponseDto(Event event) {
		this.id = event.getId();
		this.name = event.getName();
		this.content = event.getContent();
		this.startTime = event.getStartTime();
		this.endTime = event.getEndTime();
		this.quantity = event.getQuantity();
		this.finished = event.isFinished();
	}
}
