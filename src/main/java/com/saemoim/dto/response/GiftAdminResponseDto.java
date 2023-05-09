package com.saemoim.dto.response;

import com.saemoim.domain.Gift;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GiftAdminResponseDto {
	private Long eventId;
	private String eventName;
	private Long userId;
	private String username;
	private Long giftId;
	private boolean received;

	public GiftAdminResponseDto(Gift gift) {
		this.eventId = gift.getEventId();
		this.eventName = gift.getEventName();
		this.userId = gift.getUserId();
		this.username = gift.getUsername();
		this.giftId = gift.getId();
		this.received = gift.isSend();
	}
}
