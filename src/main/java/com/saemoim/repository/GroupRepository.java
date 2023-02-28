package com.saemoim.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.saemoim.domain.Category;
import com.saemoim.domain.Group;

public interface GroupRepository extends JpaRepository<Group, Long> {
	boolean existsByName(String name);

	Slice<Group> findAllByOrderByCreatedAtDesc(Pageable pageable);

	List<Group> findAllByUser_IdOrderByCreatedAtDesc(Long userId);

	@Query("select g from sae_group g where g.category = :category and g.status = 'OPEN'")
	Slice<Group> findByCategoryAndStatusIsOpen(@Param("category") Category category, Pageable pageable);

	@Query("select g from sae_group g where g.category = :category and g.status = 'CLOSE'")
	Slice<Group> findByCategoryAndStatusIsClose(@Param("category") Category category, Pageable pageable);

	Slice<Group> findByCategory(Category category, Pageable pageable);

	Slice<Group> findAllByNameContainingOrderByCreatedAtDesc(String groupName, Pageable pageable);

	@Query(value = "select distinct g from sae_group g join fetch g.user")
	List<Group> findAll();

	@Query(value = "select distinct g from sae_group g where g.user.id = :userId")
	List<Group> findByUser_userId(@Param("userId") Long userId);
}
