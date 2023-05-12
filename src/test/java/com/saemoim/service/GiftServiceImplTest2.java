package com.saemoim.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.saemoim.domain.Event;
import com.saemoim.repository.EventRepository;
import com.saemoim.repository.GiftRepository;
import com.saemoim.repository.UserRepository;

@SpringBootTest
class GiftServiceImplTest2 {
	@Autowired
	private GiftServiceImpl giftService;
	@Autowired
	private GiftRepository giftRepository;
	@Autowired
	private EventRepository eventRepository;
	@Autowired
	private UserRepository userRepository;

	@Test
	@DisplayName("concurrency test")
	@Transactional
	void concurrency() throws InterruptedException {
		//given
		final int count = 30;
		final long eventId = 3L;

		ExecutorService executorService = Executors.newFixedThreadPool(count);
		CountDownLatch countDownLatch = new CountDownLatch(count);

		//when
		for (int i = 1; i <= count; i++) {
			Long userId = Long.valueOf(i);
			executorService.execute(() -> {
				giftService.applyEvent(eventId, userId);
				countDownLatch.countDown();
			});
			Thread.sleep(30);
		}
		countDownLatch.await();

		Event event = eventRepository.findById(eventId).orElseThrow();
		//then
		assertEquals(0, event.getQuantity());
	}
}