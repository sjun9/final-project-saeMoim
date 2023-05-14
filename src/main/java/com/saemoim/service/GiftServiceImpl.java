package com.saemoim.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saemoim.domain.Event;
import com.saemoim.domain.Gift;
import com.saemoim.domain.User;
import com.saemoim.dto.response.GiftAdminResponseDto;
import com.saemoim.dto.response.GiftUserResponseDto;
import com.saemoim.exception.ErrorCode;
import com.saemoim.repository.EventRepository;
import com.saemoim.repository.GiftRepository;
import com.saemoim.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GiftServiceImpl implements GiftService {
	private final GiftRepository giftRepository;
	private final EventRepository eventRepository;
	private final UserRepository userRepository;

	@Override
	@Transactional(readOnly = true)
	public List<GiftUserResponseDto> getGifts(Long userId) {
		return giftRepository.findByUser_IdOrderByCreatedAtDesc(userId)
			.stream().map(GiftUserResponseDto::new).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<GiftAdminResponseDto> getGiftsByEventId(Long eventId) {
		return giftRepository.findByEvent_IdOrderByCreatedAt(eventId)
			.stream().map(GiftAdminResponseDto::new).toList();
	}

	@Override
	@Transactional
	public void applyEvent(Long eventId, Long userId) {
		Event event = eventRepository.findByIdWithPessimisticLock(eventId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_EVENT.getMessage())
		);
		if (LocalDateTime.now().isBefore(event.getStartTime())) {
			throw new IllegalArgumentException(ErrorCode.NOT_OVER_START_TIME.getMessage());
		} else if (LocalDateTime.now().isAfter(event.getEndTime())) {
			throw new IllegalArgumentException(ErrorCode.FINISHED_EVENT.getMessage());
		}
		User user = userRepository.findById(userId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage())
		);
		if (giftRepository.existsByEvent_IdAndUser_Id(eventId, userId)) {
			throw new IllegalArgumentException(ErrorCode.DUPLICATED_EVENT_USER.getMessage());
		}

		if (event.getQuantity() > 0) {
			event.decreaseQuantity();
			if (event.getQuantity() == 0) {
				event.finishEvent();
			}
			Gift gift = new Gift(event, user);
			giftRepository.save(gift);
		} else {
			throw new IllegalArgumentException(ErrorCode.FINISHED_EVENT.getMessage());
		}
	}

	@Override
	@Transactional
	public void updateSend(Long giftId) {
		Gift gift = giftRepository.findById(giftId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_GIFT.getMessage())
		);

		gift.updateSend(true);
		giftRepository.save(gift);
	}

	@Transactional
	@Scheduled(cron = "${schedules.cron.event}")
	public void scheduledEvent() {
		List<Event> events = eventRepository.findAllByFinishedIsFalseOrderByCreatedAtDesc()
			.stream()
			.filter(e -> LocalDateTime.now().isAfter(e.getEndTime()))
			.toList();

		for (Event event : events) {
			event.finishEvent();
		}
	}
}
