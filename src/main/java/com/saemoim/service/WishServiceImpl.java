package com.saemoim.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saemoim.domain.Group;
import com.saemoim.domain.User;
import com.saemoim.domain.Wish;
import com.saemoim.dto.response.GroupResponseDto;
import com.saemoim.exception.ErrorCode;
import com.saemoim.repository.GroupRepository;
import com.saemoim.repository.UserRepository;
import com.saemoim.repository.WishRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WishServiceImpl implements WishService {
	private final WishRepository wishRepository;
	private final UserRepository userRepository;
	private final GroupRepository groupRepository;

	@Transactional(readOnly = true)
	@Override
	public List<GroupResponseDto> getWishGroups(Long userId) {
		User user = _getUserById(userId);

		return wishRepository.findAllByUserOrderByCreatedAtDesc(user)
			.stream().map(Wish::getGroup).map(GroupResponseDto::new).toList();
	}

	@Transactional
	@Override
	public void addWishGroup(Long groupId, Long userId) {
		Group group = groupRepository.findById(groupId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_GROUP.getMessage())
		);
		User user = _getUserById(userId);
		if (wishRepository.existsByUserAndGroup(user, group)) {
			throw new IllegalArgumentException(ErrorCode.DUPLICATED_WISH.getMessage());
		}
		wishRepository.save(new Wish(group, user));
		group.addWishCount();
	}

	@Transactional
	@Override
	public void deleteWishGroup(Long groupId, Long userId) {
		Group group = groupRepository.findById(groupId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_GROUP.getMessage())
		);
		User user = _getUserById(userId);
		Wish wish = wishRepository.findByUserAndGroup(user, group).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_WISH.getMessage())
		);
		wishRepository.delete(wish);
		group.subtractWishCount();
	}

	private User _getUserById(Long userId) {
		return userRepository.findById(userId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage())
		);
	}
}
