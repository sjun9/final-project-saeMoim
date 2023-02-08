package com.saemoim.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.saemoim.dto.response.StatusResponseDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public StatusResponseDto handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		return new StatusResponseDto(HttpStatus.BAD_REQUEST, e.getAllErrors().get(0).getDefaultMessage());
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public StatusResponseDto handleIllegalArgumentException(IllegalArgumentException e) {
		return new StatusResponseDto(HttpStatus.BAD_REQUEST, e.getMessage());
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public StatusResponseDto handleRuntimeException(Exception e) {
		log.info("Internal Server Error", e);
		return new StatusResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
	}
}
