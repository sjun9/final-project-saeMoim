package com.saemoim.service;

import java.util.List;

import com.saemoim.dto.response.GiftAdminResponseDto;
import com.saemoim.dto.response.GiftUserResponseDto;

public interface GiftService {
	List<GiftUserResponseDto> getGifts(Long userId);

	List<GiftAdminResponseDto> getGiftsByEventId(Long eventId);

	void applyEvent(Long eventId, Long userId);

	void updateSend(Long giftId);
}
