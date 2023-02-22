package com.saemoim.service;

import java.util.List;

import com.saemoim.dto.response.ApplicationResponseDto;

public interface ApplicationService {
	List<ApplicationResponseDto> getMyApplications(Long userId);

	void applyGroup(Long groupId, Long userId);

	void cancelApplication(Long applicationId, String username);

	List<ApplicationResponseDto> getApplications(String username);

	void permitApplication(Long applicationId, String username);

	void rejectApplication(Long applicationId, String username);
}
