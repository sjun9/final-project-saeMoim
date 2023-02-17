package com.saemoim.fileUpload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileRequestDto {

	private Long id;
	private String originFilename;
	private String storeFilename;
	private String filePath;
}
