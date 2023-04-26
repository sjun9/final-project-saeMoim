package com.saemoim.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.saemoim.domain.Group;

public interface GroupRepository extends JpaRepository<Group, Long>, GroupRepositoryCustom {
	boolean existsByName(String name);

	// @Query("select distinct g from sae_group g join fetch g.user join fetch g.category order by g.id desc")
	// Slice<Group> findAllByOrderByGroupIdDesc(Pageable pageable);

	List<Group> findAllByUser_IdOrderByCreatedAtDesc(Long userId);

	@Query("select distinct g from sae_group g join fetch g.user join fetch g.category where g.name like %:name% order by g.createdAt desc ")
	Slice<Group> findAllByNameContainingOrderByCreatedAtDesc(@Param("name") String groupName, Pageable pageable);

	@Query(value = "select distinct g from sae_group g join fetch g.user join fetch g.category")
	List<Group> findAll();

	@Query(value = "select distinct g from sae_group g where g.user.id = :userId")
	List<Group> findByUser_userId(@Param("userId") Long userId);
}
