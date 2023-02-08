package com.saemoim.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saemoim.domain.Group;

public interface GroupRepository extends JpaRepository<Group, Long> {
}
