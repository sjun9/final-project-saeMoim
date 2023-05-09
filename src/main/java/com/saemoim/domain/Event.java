package com.saemoim.domain;

import java.time.LocalDateTime;

import com.saemoim.dto.request.EventRequestDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Event extends TimeStamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String name;

	@Column(nullable = false)
	private String content;

	@Column(nullable = false)
	private LocalDateTime startTime;

	@Column(nullable = false)
	private LocalDateTime endTime;

	@Column(nullable = false)
	private int quantity;

	@Column(nullable = false)
	private boolean finished;

	@Builder
	public Event(String name, String content, LocalDateTime startTime, LocalDateTime endTime, int quantity, boolean finished) {
		this.name = name;
		this.content = content;
		this.startTime = startTime;
		this.endTime = endTime;
		this.quantity = quantity;
		this.finished = finished;
	}

	public void updateEvent(EventRequestDto requestDto) {
		this.name = requestDto.getName();
		this.content = requestDto.getContent();
		this.startTime = requestDto.getStartTime();
		this.endTime = requestDto.getEndTime();
		this.quantity = requestDto.getQuantity();
		this.finished = requestDto.isFinished();
	}

	public void decreaseQuantity() {
		this.quantity--;
	}

	public void finishEvent() {
		this.finished = true;
	}
}
