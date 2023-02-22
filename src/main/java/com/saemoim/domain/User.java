package com.saemoim.domain;

import com.saemoim.domain.enums.UserRoleEnum;
import com.saemoim.dto.request.ProfileRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "users")
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class User extends TimeStamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false, unique = true)
	private String email;
	@Column(nullable = false)
	private String password;
	@Column(nullable = false, unique = true)
	private String username;
	// 회원가입 시, content 적는 게 없음. 논의 필요. - 해결
	@Column(nullable = false)
	private String content;
	@Column
	private Integer banCount;
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private UserRoleEnum role;

	public User(String email, String password, String username, UserRoleEnum role) {
		this.email = email;
		this.password = password;
		this.username = username;
		this.role = role;
		this.content = "안녕하세요. 잘 부탁드립니다.";
		this.banCount = 0;
	}

	public void plusBanCount() {
		this.banCount++;
	}

	public void updateStatus(UserRoleEnum role) {
		this.role = role;
	}

	public boolean isBanned() {
		return this.role.equals(UserRoleEnum.REPORT);
	}

	public void updatePasswordToTemp(String password) {
		this.password = password;
	}

	public void updateProfile(ProfileRequestDto profileRequestDto, String changedPassword) {
		this.password = changedPassword;
		this.content = profileRequestDto.getContent();
	}

}
