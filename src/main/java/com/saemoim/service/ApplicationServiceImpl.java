package com.saemoim.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saemoim.domain.Application;
import com.saemoim.domain.Group;
import com.saemoim.domain.User;
import com.saemoim.dto.response.ApplicationResponseDto;
import com.saemoim.exception.ErrorCode;
import com.saemoim.repository.ApplicationRepository;
import com.saemoim.repository.GroupRepository;
import com.saemoim.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

	private final ApplicationRepository applicationRepository;
	private final UserRepository userRepository;
	private final GroupRepository groupRepository;

	@Transactional(readOnly = true)
	@Override
	public List<ApplicationResponseDto> getMyApplications(String username) {
		User user = userRepository.findByUsername(username).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage())
		);
		List<Application> applications = applicationRepository.findAllByUserOrderByCreatedAtDesc(user);
		return applications.stream().map(ApplicationResponseDto::new).toList();
	}

	@Transactional
	@Override
	public void applyGroup(Long groupId, String username) {
		Group group = groupRepository.findById(groupId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_GROUP.getMessage())
		);
		User user = userRepository.findByUsername(username).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage())
		);
		if (applicationRepository.existsByUserAndGroup(user, group)) {
			throw new IllegalArgumentException(ErrorCode.DUPLICATED_APPLICATION.getMessage());
		}
		applicationRepository.save(new Application(user, group));
	}

	@Transactional
	@Override
	public void cancelApplication(Long applicationId, String username) {
		User user = userRepository.findByUsername(username).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage())
		);
		Application application = applicationRepository.findById(applicationId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_APPLICATION.getMessage())
		);
		if (!application.isRightUserWhoApllied(user.getUsername())) {
			throw new IllegalArgumentException(ErrorCode.INVALID_USER.getMessage());
		}
		applicationRepository.delete(application);
	}

	@Transactional(readOnly = true)
	@Override
	public List<ApplicationResponseDto> getApplications(Long groupId, String username) {
		Group group = groupRepository.findById(groupId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_GROUP.getMessage())
		);
		if (!group.isLeader(username)) {
			throw new IllegalArgumentException(ErrorCode.INVALID_USER.getMessage());
		}
		List<Application> applications = applicationRepository.findAllByGroupOrderByCreatedAt(group);

		return applications.stream().map(ApplicationResponseDto::new).toList();
	}

	@Transactional
	@Override
	public void permitApplication(Long applicationId, String username) {

		// 어플리케이션 상태 변화 + 파티시펀트 값 추가 두개를 해야하니 연
		// participant서비스에서 생성하는 메서드를 여기서 호출.
		// 연관관계가 안맺어져 있음
		// 모임에서 모임참가자랑 원투매니를 맺어
	}

	@Transactional
	@Override
	public void rejectApplication(Long applicationId, String username) {

	}
}
