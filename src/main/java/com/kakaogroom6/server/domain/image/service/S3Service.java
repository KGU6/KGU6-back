package com.kakaogroom6.server.domain.image.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class S3Service {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private String PROFILE_IMG_DIR = "profile/";
    private String MEMBER_IMG_DIR = "member/";

    public String savePhoto(MultipartFile multipartFile, Long memberId) throws IOException {
        File uploadFile = convert(multipartFile)  // 파일 변환할 수 없으면 에러
                .orElseThrow(() -> new IllegalArgumentException("error: MultipartFile -> File convert fail"));
        return upload(uploadFile, MEMBER_IMG_DIR, memberId);
    }

    public String saveProfileImg(MultipartFile multipartFile, Long memberId) throws IOException {
        File uploadFile = convert(multipartFile)  // 파일 변환할 수 없으면 에러
                .orElseThrow(() -> new IllegalArgumentException("error: MultipartFile -> File convert fail"));
        return upload(uploadFile, PROFILE_IMG_DIR, memberId);
    }

    // S3로 파일 업로드하기
    private String upload(File uploadFile, String dirName, Long memberId) {
        String fileName = dirName + memberId  + "/" + UUID.randomUUID() + uploadFile.getName();   // S3에 저장된 파일 이름
        String uploadImageUrl = putS3(uploadFile, fileName); // s3로 업로드
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    // S3로 업로드
    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(
                CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    // 로컬에 저장된 이미지 지우기
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("File delete success");
            return;
        }
        log.info("File delete fail");
    }

    private Optional<File> convert(MultipartFile file) {
        if (file.isEmpty() || file.getOriginalFilename() == null) {
            log.error("File is empty or filename is null");
            return Optional.empty();
        }

        try {
            File convertFile = new File(System.getProperty("java.io.tmpdir") + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename());
            if (convertFile.createNewFile()) {
                try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                    fos.write(file.getBytes());
                }
                return Optional.of(convertFile);
            } else {
                log.error("File already exists: {}", convertFile.getAbsolutePath());
            }
        } catch (IOException e) {
            log.error("File conversion failed: {}", e.getMessage());
        }
        return Optional.empty();
    }


    public void createDir(String bucketName, String folderName) {
        amazonS3Client.putObject(bucketName, folderName + "/", new ByteArrayInputStream(new byte[0]), new ObjectMetadata());
    }
}

