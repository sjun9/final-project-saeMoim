package com.saemoim.fileUpload;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class FileController {

	private final FileService fileService;

	@PostMapping("/upload")
	public Long uploadFile(@RequestPart("file")MultipartFile file) throws IOException {
		return fileService.saveFile(file);
	}


}
