package com.saemoim.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saemoim.dto.response.ApplicationResponseDto;
import com.saemoim.dto.response.StatusResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {
	@Transactional(readOnly = true)
	@Override
	public List<ApplicationResponseDto> getMyApplications(String username) {
		return null;
	}

	@Transactional
	@Override
	public StatusResponseDto applyGroup(Long groupId, String username) {
		return null;
	}

	@Transactional
	@Override
	public StatusResponseDto cancelApplication(Long applicationId, String username) {
		return null;
	}

	@Transactional(readOnly = true)
	@Override
	public List<ApplicationResponseDto> getApplications(Long groupId, String username) {
		return null;
	}

	@Transactional
	@Override
	public StatusResponseDto permitApplication(Long applicationId, String username) {
		return null;
	}

	@Transactional
	@Override
	public StatusResponseDto rejectApplication(Long applicationId, String username) {
		return null;
	}
}
