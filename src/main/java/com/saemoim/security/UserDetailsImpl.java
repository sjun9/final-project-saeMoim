package com.saemoim.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.saemoim.domain.enums.UserRoleEnum;

public class UserDetailsImpl implements UserDetails {

	private final String username;    // 인증된 유저 객체
	private final Long id;
	private final UserRoleEnum role;

	private final Collection<GrantedAuthority> authorities = new ArrayList<>();

	public UserDetailsImpl(String username, Long id, UserRoleEnum role) {
		this.username = username;
		this.id = id;
		this.role = role;
		authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public Long getId() {
		return this.id;
	}

	public UserRoleEnum getRole() {
		return this.role;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
