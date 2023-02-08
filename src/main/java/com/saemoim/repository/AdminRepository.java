package com.saemoim.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saemoim.domain.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
