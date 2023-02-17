package com.saemoim.fileUpload;

import java.io.File;
import java.io.IOException;

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
	public void uploadFile(@RequestPart("file")MultipartFile file) throws IOException {
		String originalFilename = file.getOriginalFilename();
		String storeFilename =  originalFilename;
		String savePath = System.getProperty("user.dir") + "\\files";
		// 저장 주소가 없으면 생성. 로컬 저장소 위치이므로 s3 연결하게 되면 불필요.
		if (!new File(savePath).exists()) {
			try{
				new File(savePath).mkdirs();
			}
			catch(Exception e){
				e.getStackTrace();
			}
		}
		String filePath = savePath + "\\" + storeFilename;
		file.transferTo(new File(filePath));

		FileRequestDto fileRequest = FileRequestDto.builder()
			.originFilename(originalFilename)
			.storeFilename(storeFilename)
			.filePath(filePath)
			.build();

		fileService.saveFile(fileRequest);
	}
}
