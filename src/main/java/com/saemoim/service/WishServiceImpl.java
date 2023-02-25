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

	@Transactional
	@Override
	public List<GroupResponseDto> getWishGroups(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage())
		);
		List<Wish> wishes = wishRepository.findAllByUserOrderByCreatedAtDesc(user);
		return wishes.stream().map(Wish::getGroup).map(GroupResponseDto::new).toList();
	}

	@Transactional
	@Override
	public void wishGroup(Long groupId, Long userId) {
		Group group = groupRepository.findById(groupId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_GROUP.getMessage())
		);
		User user = userRepository.findById(userId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage())
		);
		if (!wishRepository.existsByUserAndGroup(user, group)) {
			Wish wish = new Wish(group, user);
			wishRepository.save(wish);
			group.addWishCount();
		} else {
			throw new IllegalArgumentException(ErrorCode.DUPLICATED_WISH.getMessage());
		}
	}

	@Transactional
	@Override
	public void deleteWishGroup(Long groupId, Long userId) {
		Group group = groupRepository.findById(groupId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_GROUP.getMessage())
		);
		User user = userRepository.findById(userId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage())
		);
		Wish wish = wishRepository.findByUserAndGroup(user, group).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_WISH.getMessage())
		);
		wishRepository.delete(wish);
		group.subtractWishCount();
	}
}
