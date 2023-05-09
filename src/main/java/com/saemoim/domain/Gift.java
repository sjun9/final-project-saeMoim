package com.saemoim.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Gift extends TimeStamped{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "event_id")
	private Event event;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Column(nullable = false)
	private boolean send;

	public Gift(Event event, User user) {
		this.event = event;
		this.user = user;
		this.send = false;
	}

	public Long getEventId() {
		return this.event.getId();
	}

	public String getEventName() {
		return this.event.getName();
	}

	public Long getUserId() {
		return this.user.getId();
	}
	public String getUsername() {
		return this.user.getUsername();
	}

	public void updateSend(boolean send) {
		this.send = send;
	}
}
