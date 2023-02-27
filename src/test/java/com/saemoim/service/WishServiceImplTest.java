package com.saemoim.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.saemoim.domain.Group;
import com.saemoim.domain.User;
import com.saemoim.domain.Wish;
import com.saemoim.repository.GroupRepository;
import com.saemoim.repository.UserRepository;
import com.saemoim.repository.WishRepository;

@ExtendWith(MockitoExtension.class)
class WishServiceImplTest {
	@Mock
	private WishRepository wishRepository;
	@Mock
	private UserRepository userRepository;
	@Mock
	private GroupRepository groupRepository;

	@InjectMocks
	private WishServiceImpl wishService;

	@Test
	@DisplayName("찜한 모임 조회")
	void getWishGroups() {
		// given
		Long userId = 1L;
		var user = User.builder().build();
		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

		// when
		wishService.getWishGroups(userId);

		// then
		verify(wishRepository).findAllByUserOrderByCreatedAtDesc(user);
	}

	@Test
	@DisplayName("모임찜하깅")
	void wishGroup() {
		// given
		var groupId = 1L;
		var userId = 1L;
		var group = Group.builder().wishCount(2).build();
		var user = User.builder().build();

		when(groupRepository.findById(anyLong())).thenReturn(Optional.of(group));
		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
		when(wishRepository.existsByUserAndGroup(any(User.class), any(Group.class))).thenReturn(false);
		// when
		wishService.addWishGroup(groupId, userId);

		// then
		verify(wishRepository).save(any(Wish.class));
		assertThat(group.getWishCount()).isEqualTo(3);
	}

	@Test
	@DisplayName("찜하기 취소!")
	void deleteWishGroup() {
		// given
		var groupId = 1L;
		var userId = 1L;
		var group = Group.builder().wishCount(2).build();
		var user = User.builder().build();
		var wish = new Wish(group, user);

		when(groupRepository.findById(anyLong())).thenReturn(Optional.of(group));
		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
		when(wishRepository.findByUserAndGroup(any(User.class), any(Group.class))).thenReturn(Optional.of(wish));

		// when
		wishService.deleteWishGroup(groupId, userId);

		// then
		verify(wishRepository).delete(any(Wish.class));
		assertThat(group.getWishCount()).isEqualTo(1);
	}
}