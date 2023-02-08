package com.saemoim.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saemoim.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
