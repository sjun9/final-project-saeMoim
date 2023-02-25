package com.saemoim.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.saemoim.dto.response.GenericsResponseDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {
	@ExceptionHandler(DataIntegrityViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<GenericsResponseDto> handleDataIntegrityViolationException(
		DataIntegrityViolationException e) {
		return ResponseEntity.badRequest().body(new GenericsResponseDto(e.getMessage()));
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<GenericsResponseDto> handleIllegalArgumentException(IllegalArgumentException e) {
		return ResponseEntity.badRequest().body(new GenericsResponseDto(e.getMessage()));
	}

	@ExceptionHandler(NullPointerException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<GenericsResponseDto> handleNullPointerException(NullPointerException e) {
		return ResponseEntity.badRequest().body(new GenericsResponseDto(e.getMessage()));
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<GenericsResponseDto> handleRuntimeException(Exception e) {
		log.info("Internal Server Error", e);
		return ResponseEntity.internalServerError()
			.body(new GenericsResponseDto(ErrorCode.INTERNAL_SERVER_ERROR.getMessage()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<GenericsResponseDto> MethodArgumentNotValidException(MethodArgumentNotValidException e) {
		return ResponseEntity.badRequest().body(new GenericsResponseDto(e.getAllErrors().get(0).getDefaultMessage()));
	}

	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ResponseEntity<GenericsResponseDto> handleAccessDeniedException(AccessDeniedException e) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new GenericsResponseDto(e.getMessage()));
	}
}
