package com.saemoim.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.saemoim.domain.User;
import com.saemoim.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;

	//
	@Override
	public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {

		User user = userRepository.findByEmail(userEmail)
			.orElseThrow(() -> new UsernameNotFoundException("해당 이용자가 업솝니다."));

		return new UserDetailsImpl(user,user.getEmail(),user.getPassword());
	}
}
