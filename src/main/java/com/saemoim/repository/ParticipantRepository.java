package com.saemoim.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.saemoim.domain.Participant;
import com.saemoim.domain.User;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
	@Query(value = "select p from Participant p join fetch p.group where p.group.id in (:groupIdList)")
	List<Participant> findAllParticipants(@Param("groupIdList") List<Long> groupIdList);

	List<Participant> findAllByUserOrderByCreatedAtDesc(User user);
}
