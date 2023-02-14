package com.saemoim.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saemoim.domain.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
	List<Tag> findAllByGroupId(Long groupId);
}
