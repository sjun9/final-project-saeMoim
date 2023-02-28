package com.saemoim.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saemoim.domain.Application;
import com.saemoim.domain.Group;
import com.saemoim.domain.Participant;
import com.saemoim.domain.User;
import com.saemoim.dto.response.ApplicationResponseDto;
import com.saemoim.exception.ErrorCode;
import com.saemoim.repository.ApplicationRepository;
import com.saemoim.repository.GroupRepository;
import com.saemoim.repository.ParticipantRepository;
import com.saemoim.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

	private final ApplicationRepository applicationRepository;
	private final UserRepository userRepository;
	private final GroupRepository groupRepository;
	private final ParticipantRepository participantRepository;

	@Transactional(readOnly = true)
	@Override
	public List<ApplicationResponseDto> getMyApplications(Long userId) {
		User user = _getUserById(userId);
		List<Application> applications = applicationRepository.findAllByUserOrderByCreatedAtDesc(user);
		return applications.stream().map(ApplicationResponseDto::new).toList();
	}

	@Transactional
	@Override
	public void applyGroup(Long groupId, Long userId) {
		Group group = groupRepository.findById(groupId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_GROUP.getMessage())
		);
		User user = _getUserById(userId);
		if (applicationRepository.existsByUserAndGroup(user, group)) {
			throw new IllegalArgumentException(ErrorCode.DUPLICATED_APPLICATION.getMessage());
		}
		if (group.isLeader(user.getId())) {
			throw new IllegalArgumentException(ErrorCode.INVALID_USER.getMessage());
		}
		applicationRepository.save(new Application(user, group));
	}

	@Transactional
	@Override
	public void deleteApplication(Long applicationId, Long userId) {
		Application application = _getApplicationById(applicationId);
		if (!application.isRightUserWhoApplied(userId)) {
			throw new IllegalArgumentException(ErrorCode.INVALID_USER.getMessage());
		}
		applicationRepository.delete(application);
	}

	@Transactional(readOnly = true)
	@Override
	public List<ApplicationResponseDto> getApplications(Long userId) {
		List<Group> groups = groupRepository.findByUser_userId(userId);
		List<Application> applications = applicationRepository.findAllByGroups(groups);

		return applications.stream().map(ApplicationResponseDto::new).toList();
	}

	@Transactional
	@Override
	public void permitApplication(Long applicationId, Long userId) {
		Application application = _getApplicationById(applicationId);
		Group group = groupRepository.findById(application.getGroupId()).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_GROUP.getMessage())
		);
		if (!group.isLeader(userId)) {
			throw new IllegalArgumentException(ErrorCode.INVALID_USER.getMessage());
		}
		application.permit();
		applicationRepository.save(application);

		// 모임 참여자 추가
		User user = _getUserById(application.getUserId());
		Participant participant = new Participant(user, group);
		participantRepository.save(participant);
	}

	@Transactional
	@Override
	public void rejectApplication(Long applicationId, Long userId) {
		Application application = _getApplicationById(applicationId);
		Group group = groupRepository.findById(application.getGroupId()).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_GROUP.getMessage())
		);
		if (!group.isLeader(userId)) {
			throw new IllegalArgumentException(ErrorCode.INVALID_USER.getMessage());
		}
		application.reject();
		applicationRepository.save(application);
	}

	private User _getUserById(Long userId) {
		return userRepository.findById(userId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage())
		);
	}

	private Application _getApplicationById(Long applicationId) {
		return applicationRepository.findById(applicationId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_APPLICATION.getMessage())
		);
	}
}
