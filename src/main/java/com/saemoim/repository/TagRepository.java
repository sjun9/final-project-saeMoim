package com.saemoim.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import com.saemoim.domain.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
	Slice<Tag> findAllByNameContaining(String name, Pageable pageable);
}
