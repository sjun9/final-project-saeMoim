package com.saemoim.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.saemoim.domain.Group;

public interface GroupRepositoryCustom {
	Slice<Group> findAllByOrderByCreatedAtDesc(Long groupId, Pageable pageable);

	Slice<Group> findByCategoryAndStatusByOrderByCreateAtDesc(Long categoryId, String status, Pageable pageable);
}
