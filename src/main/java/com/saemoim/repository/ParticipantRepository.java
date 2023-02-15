package com.saemoim.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saemoim.domain.Group;
import com.saemoim.domain.Participant;
import com.saemoim.domain.User;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
	List<Participant> findAllByGroupOrderByCreatedAtDesc(Group group);

	List<Participant> findAllByUser_IdOrderByCreatedAtDesc(Long userId);

	Optional<Participant> findByGroupAndUser(Group group, User user);
}
