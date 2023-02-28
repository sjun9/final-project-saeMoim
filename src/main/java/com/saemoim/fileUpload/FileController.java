package com.saemoim.fileUpload;

import java.io.IOException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class FileController {

	private final AWSS3Service awss3Service;

	@PostMapping("/upload")
	public String upload(@RequestParam("data") MultipartFile multipartFile) throws IOException {
		return awss3Service.upload(multipartFile, "static");
	}
}
