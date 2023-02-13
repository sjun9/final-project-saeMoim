package com.saemoim.repository;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import com.saemoim.domain.Participant;

@SpringBootTest
@Transactional
@Commit
class ParticipantRepositoryTest {

	@Autowired
	ParticipantRepository participantRepository;

	@Test
	void findAllParticipantsByGroup() {

		List<Participant> p = participantRepository.findAllParticipants(null);
	}
}