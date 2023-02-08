package com.saemoim.dto.response;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class StatusResponseDto {
	private final HttpStatus httpStatus;
	private final String message;

}
