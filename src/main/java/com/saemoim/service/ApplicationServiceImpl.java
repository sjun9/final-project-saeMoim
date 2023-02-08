package com.saemoim.service;

import java.util.List;

import com.saemoim.dto.response.ApplicationResponseDto;
import com.saemoim.dto.response.StatusResponseDto;

public class ApplicationServiceImpl implements ApplicationService {
	@Override
	public List<ApplicationResponseDto> getMyApplications(String username) {
		return null;
	}

	@Override
	public StatusResponseDto applyGroup(Long groupId, String username) {
		return null;
	}

	@Override
	public StatusResponseDto cancelApplication(Long applicationId, String username) {
		return null;
	}

	@Override
	public List<ApplicationResponseDto> getApplications(Long groupId, String username) {
		return null;
	}

	@Override
	public StatusResponseDto permitApplication(Long applicationId, String username) {
		return null;
	}

	@Override
	public StatusResponseDto rejectApplication(Long applicationId, String username) {
		return null;
	}
}
