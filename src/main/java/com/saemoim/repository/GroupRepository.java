package com.saemoim.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.saemoim.domain.Category;
import com.saemoim.domain.Group;

public interface GroupRepository extends JpaRepository<Group, Long> {
	boolean existsByName(String name);

	List<Group> findAllByOrderByCreatedAtDesc(Pageable pageable);

	List<Group> findAllByUser_IdOrderByCreatedAtDesc(Long userId);

	List<Group> findAllByCategoryOrderByCreatedAtDesc(Category category);

	List<Group> findAllByNameContainingOrderByCreatedAtDesc(String name);

	@Query(value = "select distinct g from sae_group g join fetch g.user")
	List<Group> findAll();

	@Query(value = "select distinct g from sae_group g where g.user.username = :username")
	List<Group> findByUser_username(@Param("username") String username);
}
