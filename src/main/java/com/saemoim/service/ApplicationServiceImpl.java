package com.saemoim.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saemoim.dto.response.ApplicationResponseDto;

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
	public void applyGroup(Long groupId, String username) {

	}

	@Transactional
	@Override
	public void cancelApplication(Long applicationId, String username) {

	}

	@Transactional(readOnly = true)
	@Override
	public List<ApplicationResponseDto> getApplications(Long groupId, String username) {
		return null;
	}

	@Transactional
	@Override
	public void permitApplication(Long applicationId, String username) {

	public StatusResponseDto permitApplication(Long applicationId, String username) {
		// 어플리케이션 상태 변화 + 파티시펀트 값 추가 두개를 해야하니 연
		// participant서비스에서 생성하는 메서드를 여기서 호출.
		// 연관관계가 안맺어져 있음
		// 모임에서 모임참가자랑 원투매니를 맺어
		return null;
	}

	@Transactional
	@Override
	public void rejectApplication(Long applicationId, String username) {

	}
}
