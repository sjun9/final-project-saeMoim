package com.saemoim.service;

import java.util.List;

import com.saemoim.dto.response.ApplicationResponseDto;

public interface ApplicationService {
	List<ApplicationResponseDto> getMyApplications(Long userId);

	void applyGroup(Long groupId, Long userId);

	void deleteApplication(Long applicationId, Long userId);

	List<ApplicationResponseDto> getApplications(Long userId);

	void permitApplication(Long applicationId, Long userId);

	void rejectApplication(Long applicationId, Long userId);
}
