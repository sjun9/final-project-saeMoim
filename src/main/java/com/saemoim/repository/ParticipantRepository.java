package com.saemoim.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saemoim.domain.Participant;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
	List<Participant> findAllByGroup_IdOrderByCreatedAtDesc(Long groupId);

	List<Participant> findAllByUser_IdOrderByCreatedAtDesc(Long userId);

	Optional<Participant> findByGroup_IdAndUser_Id(Long groupId, Long userId);

	boolean existsByUser_IdAndGroup_Id(Long userId, Long GroupId);
}
