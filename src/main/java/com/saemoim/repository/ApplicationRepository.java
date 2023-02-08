package com.saemoim.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saemoim.domain.Application;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
}
