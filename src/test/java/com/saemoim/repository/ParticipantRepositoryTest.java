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

	@Test
	void findAllByGroup_IdOrderByCreatedAtDesc() {
		participantRepository.findAllByGroup_IdOrderByCreatedAtDesc(null);
	}

	@Test
	void testFindAllByUser_IdOrderByCreatedAtDesc() {
		participantRepository.findAllByUser_IdOrderByCreatedAtDesc(null);
	}

	@Test
	void findByGroup_IdAndUser_Id() {
		participantRepository.findByGroup_IdAndUser_Id(null, null);
	}
}