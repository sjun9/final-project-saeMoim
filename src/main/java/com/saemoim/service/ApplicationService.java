package com.saemoim.service;

import java.util.List;

import com.saemoim.dto.response.ApplicationResponseDto;
import com.saemoim.dto.response.StatusResponseDto;

public interface ApplicationService {
	List<ApplicationResponseDto> getMyApplications(String username);

	StatusResponseDto applyGroup(Long groupId, String username);

	StatusResponseDto cancelApplication(Long applicationId, String username);

	List<ApplicationResponseDto> getApplications(Long groupId, String username);

	StatusResponseDto permitApplication(Long applicationId, String username);

	StatusResponseDto rejectApplication(Long applicationId, String username);
}
