package com.saemoim.dto.response;

import com.saemoim.domain.Admin;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminResponseDto {
	private Long adminId;
	private String adminName;

	@Builder
	public AdminResponseDto(Admin admin) {
		this.adminName = admin.getUsername();
		this.adminId = admin.getId();
	}
}
