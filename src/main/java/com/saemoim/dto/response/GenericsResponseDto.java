package com.saemoim.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GenericsResponseDto<T> {
	private T data;

	public GenericsResponseDto(T data) {
		this.data = data;
	}
}
