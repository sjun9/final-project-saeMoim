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
public class GiftUserResponseDto {
	private String eventName;
	private Long giftId;
	private boolean received;

	public GiftUserResponseDto(Gift gift) {
		this.eventName = gift.getEventName();
		this.giftId = gift.getId();
		this.received = gift.isSend();
	}
}
