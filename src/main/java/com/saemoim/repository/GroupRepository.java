package com.saemoim.repository;

import java.awt.print.Pageable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saemoim.domain.Group;

public interface GroupRepository extends JpaRepository<Group, Long> {
	List<Group> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
