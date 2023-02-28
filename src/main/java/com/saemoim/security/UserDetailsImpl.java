package com.saemoim.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.saemoim.domain.enums.UserRoleEnum;

public class UserDetailsImpl implements UserDetails, OAuth2User {

	private final String username;    // 인증된 유저 객체
	private final Long id;
	private final UserRoleEnum role;
	private Map<String, Object> attributes;

	private final Collection<GrantedAuthority> authorities = new ArrayList<>();

	public UserDetailsImpl(Long id, String username, UserRoleEnum role) {
		this.id = id;
		this.username = username;
		this.role = role;
		authorities.add(new SimpleGrantedAuthority(this.role.getAuthority()));
	}

	public UserDetailsImpl(Long id, String username, UserRoleEnum role, Map<String, Object> attributes) {
		this.username = username;
		this.id = id;
		this.role = role;
		this.attributes = attributes;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
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

	@Override
	public String getName() {
		return null;
	}
}
