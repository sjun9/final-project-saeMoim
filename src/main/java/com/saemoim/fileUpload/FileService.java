package com.saemoim.fileUpload;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FileService {

	private final FileRepository fileRepository;

	@Transactional
	public void saveFile(FileRequestDto fileRequestDto) {

		// 제약조건
		// 이미지 파일인가(확장자명 검사)? 크기는?

		String originFilename = fileRequestDto.getOriginFilename();
		String storeFilename = fileRequestDto.getStoreFilename();
		String filePath = fileRequestDto.getFilePath();

		UploadFile uploadFile = UploadFile.builder()
			.originalFilename(originFilename)
			.storeFilename(storeFilename)
			.filePath(filePath)
			.build();

		fileRepository.save(uploadFile);
	}

	// @Transactional
	// public FileRequestDto getFile(Long fileId){
	//
	// }

}
