package com.saemoim.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.saemoim.domain.Category;
import com.saemoim.domain.Group;

public interface GroupRepository extends JpaRepository<Group, Long> {
	List<Group> findAllByOrderByCreatedAtDesc(Pageable pageable);

	List<Group> findAllByUser_IdOrderByCreatedAtDesc(Long userId);

	List<Group> findAllByCategoryOrderByCreatedAtDesc(Category category);

	List<Group> findAllByNameContainingOrderByCreatedAtDesc(String name);
}
