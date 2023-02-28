package com.saemoim.service;

import java.util.List;

import com.saemoim.domain.enums.UserRoleEnum;
import com.saemoim.dto.request.AdminRequestDto;
import com.saemoim.dto.response.AdminResponseDto;
import com.saemoim.dto.response.AdminTokenResponseDto;

public interface AdminService {

	AdminTokenResponseDto signInByAdmin(AdminRequestDto requestDto);

	List<AdminResponseDto> getAdmins();

	void createAdmin(AdminRequestDto requestDto);

	void deleteAdmin(Long adminId);

	String issueToken(Long id, String username, UserRoleEnum role);
}
