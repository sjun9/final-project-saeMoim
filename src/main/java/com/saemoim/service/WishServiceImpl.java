package com.saemoim.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saemoim.domain.User;
import com.saemoim.domain.Wish;
import com.saemoim.dto.response.MyGroupResponseDto;
import com.saemoim.exception.ErrorCode;
import com.saemoim.repository.UserRepository;
import com.saemoim.repository.WishRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WishServiceImpl implements WishService {
	private final WishRepository wishRepository;
	private final UserRepository userRepository;

	@Transactional
	@Override
	public List<MyGroupResponseDto> getWishGroups(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage())
		);
		List<Wish> wishes = wishRepository.findAllByUserOrderByCreatedAtDesc(user);
		return wishes.stream().map(Wish::getGroup).map(MyGroupResponseDto::new).toList();
	}

	@Transactional
	@Override
	public void wishGroup(Long groupId, Long userId) {

	}

	@Transactional
	@Override
	public void deleteWishGroup(Long groupId, Long userId) {

	}
}
