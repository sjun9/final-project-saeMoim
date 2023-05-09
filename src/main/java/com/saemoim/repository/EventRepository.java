package com.saemoim.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.saemoim.domain.Event;

import jakarta.persistence.LockModeType;

public interface EventRepository extends JpaRepository<Event, Long> {
	List<Event> findAllByFinishedIsFalseOrderByCreatedAtDesc();

	boolean existsByName(String name);

	@Query("select e from Event e where e.id = :eventId")
	@Lock(value = LockModeType.PESSIMISTIC_WRITE)
	Optional<Event> findByIdWithPessimisticLock(@Param("eventId") Long eventId);
}
