package com.saemoim.service;

import com.saemoim.dto.request.AdminRequestDto;
import com.saemoim.dto.response.TokenResponseDto;

public interface AdminService {

	TokenResponseDto signInByAdmin(AdminRequestDto requestDto);

	void createAdmin(AdminRequestDto requestDto);
}
