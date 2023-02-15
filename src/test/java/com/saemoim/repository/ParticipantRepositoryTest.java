package com.saemoim.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ParticipantRepositoryTest {

	@Autowired
	ParticipantRepository participantRepository;

	@Test
	void findAllByUser_IdOrderByCreatedAtDesc() {
		participantRepository.findAllByUser_IdOrderByCreatedAtDesc(null);
	}
}