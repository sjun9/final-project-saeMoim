package com.saemoim.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

	@ExceptionHandler(DataIntegrityViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ExceptionResponseDto handleDataIntegrityViolationException(DataIntegrityViolationException e) {
		return new ExceptionResponseDto(HttpStatus.BAD_REQUEST, e.getMessage());
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ExceptionResponseDto handleIllegalArgumentException(IllegalArgumentException e) {
		return new ExceptionResponseDto(HttpStatus.BAD_REQUEST, e.getMessage());
	}

	@ExceptionHandler(NullPointerException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ExceptionResponseDto handleNullPointerException(NullPointerException e) {
		return new ExceptionResponseDto(HttpStatus.BAD_REQUEST, e.getMessage());
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ExceptionResponseDto handleRuntimeException(Exception e) {
		log.info("Internal Server Error", e);
		return new ExceptionResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ExceptionResponseDto MethodArgumentNotValidException(MethodArgumentNotValidException e) {
		return new ExceptionResponseDto(HttpStatus.BAD_REQUEST, e.getAllErrors().get(0).getDefaultMessage());
	}

}
