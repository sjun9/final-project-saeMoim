package com.saemoim.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.saemoim.domain.User;
import com.saemoim.domain.enums.UserRoleEnum;
import com.saemoim.exception.ErrorCode;
import com.saemoim.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(userEmail)
			.orElseThrow(() -> new UsernameNotFoundException(ErrorCode.NOT_FOUND_USER.getMessage()));
		return new UserDetailsImpl(user.getUsername(), user.getId(), user.getRole());
	}

	public UserDetails loadUserInfoByJwt(String username, Long id, UserRoleEnum role) {
		return new UserDetailsImpl(username, id, role);
	}
}
