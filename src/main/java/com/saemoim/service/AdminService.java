package com.saemoim.service;

import java.util.List;

import com.saemoim.dto.request.AdminRequestDto;
import com.saemoim.dto.response.AdminResponseDto;
import com.saemoim.dto.response.TokenResponseDto;

public interface AdminService {

	TokenResponseDto signInByAdmin(AdminRequestDto requestDto);

	List<AdminResponseDto> getAdmins();

	void createAdmin(AdminRequestDto requestDto);

	void deleteAdmin(Long adminId);
}
