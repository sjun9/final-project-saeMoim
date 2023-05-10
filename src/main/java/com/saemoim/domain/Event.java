package com.saemoim.domain;

import java.time.LocalDateTime;

import com.saemoim.dto.request.EventRequestDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event extends TimeStamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "admin_id")
	private Admin admin;

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

	public void updateEvent(EventRequestDto requestDto) {
		this.name = requestDto.getName();
		this.content = requestDto.getContent();
		this.startTime = requestDto.getStartTime();
		this.endTime = requestDto.getEndTime();
		this.quantity = requestDto.getQuantity();
		this.finished = requestDto.isFinished();
	}

	public String getAdminName() {
		return this.admin.getUsername();
	}

	public void decreaseQuantity() {
		this.quantity--;
	}

	public void finishEvent() {
		this.finished = true;
	}
}
