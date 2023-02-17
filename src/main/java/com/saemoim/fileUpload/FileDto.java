// package com.saemoim.fileUpload;
//
// import java.io.File;
//
// import lombok.Builder;
// import lombok.Getter;
// import lombok.NoArgsConstructor;
// import lombok.Setter;
// import lombok.ToString;
//
// @Getter
// @Setter
// @ToString
// @NoArgsConstructor
// public class FileDto {
// 	private Long id;
// 	private String originFilename;
// 	private String storeFilename;
// 	private String filePath;
//
// 	public UploadFile toEntity() {
// 		UploadFile build = UploadFile.builder()
// 			.id(id)
// 			.originalFilename(originFilename)
// 			.storeFilename(storeFilename)
// 			.filePath(filePath)
// 			.build();
// 		return build;
// 	}
//
// 	@Builder
// 	public FileDto(Long id, String originFilename, String storeFilename, String filePath) {
// 		this.id = id;
// 		this.originFilename = originFilename;
// 		this.storeFilename = storeFilename;
// 		this.filePath = filePath;
// 	}
//
// }
