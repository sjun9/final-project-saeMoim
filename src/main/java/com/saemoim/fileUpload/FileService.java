package com.saemoim.fileUpload;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.saemoim.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FileService {

	private final FileRepository fileRepository;
	@Transactional
	public Long saveFile(MultipartFile file) throws IOException {
		// 확장자 체크
		if (file.getContentType() == null || !file.getContentType().startsWith("image")) {
			throw new IllegalArgumentException(ErrorCode.NOT_IMAGE_FILE.getMessage());
		}
		// 저장경로 설정
		String path = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";
		// 파일경로 설정
		UUID uuid = UUID.randomUUID();
		String storeFileName = uuid + "_" + file.getOriginalFilename();
		// 파일 객체 생성
		File saveFile = new File(path, storeFileName);
		// 로컬 저장소에 저장
		file.transferTo(saveFile);
		// 저장 파일에 대한 정보 저장
		UploadFile uploadFile = UploadFile.builder()
			.originalFilename(file.getOriginalFilename())
			.storeFilename(storeFileName)
			.filePath(path)
			.build();

		fileRepository.save(uploadFile);

		//

		return null;
	}

	// @Transactional
	// public FileResponseDto getFile(Long fileId){
	// 	fileRepository.
	// }

}
