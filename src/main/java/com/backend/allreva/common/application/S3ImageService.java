package com.backend.allreva.common.application;

import com.backend.allreva.common.exception.CustomException;
import com.backend.allreva.common.exception.code.GlobalErrorCode;
import com.backend.allreva.common.model.Image;
import com.backend.allreva.seat_review.command.application.dto.FileData;
import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Operations;
import io.awspring.cloud.s3.S3Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3ImageService {

    @Value("${spring.cloud.aws.s3.bucket:null}")
    private String bucketName;
    private final S3Operations s3Operations;

    public Image upload(FileData fileData) {
        if (fileData.bytes() == null || fileData.bytes().length == 0) {
            return new Image("");
        }

        // ObjectMetadata 생성 (빌더 사용)
        ObjectMetadata objectMetadata = ObjectMetadata.builder()
                .contentType("application/octet-stream") // 필요에 따라 contentType 설정
                .build();

        String storeKey = UUID.randomUUID() + "_" + fileData.filename();

        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(fileData.bytes())) {
            // S3 업로드
            S3Resource resource = s3Operations.upload(bucketName, storeKey, inputStream, objectMetadata);
            return new Image(resource.getURL().toString());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(GlobalErrorCode.SERVER_ERROR);
        }
    }

    public List<Image> upload(List<MultipartFile> imageFiles) {
        log.info("Uploading images from {} files", imageFiles.size());
        if (imageFiles == null || imageFiles.isEmpty()) {
            return Collections.emptyList();
        }
        List<Image> images = new ArrayList<>();

        for (MultipartFile file : imageFiles) {
            Image uploadedImage = upload(file);
            images.add(uploadedImage);
        }
        return images;
    }

    public Image upload(MultipartFile imageFile) {
        if (imageFile == null || imageFile.isEmpty()) {
            return new Image("");
        }
        ObjectMetadata objectMetadata = new ObjectMetadata.Builder()
                .contentType(imageFile.getContentType())
                .build();
        String storeKey = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
        return uploadOnS3(imageFile, storeKey, objectMetadata);
    }

    public void delete(String imageUrl) {
        if (StringUtils.isEmpty(imageUrl)) {
            return;
        }
        try {
            String key = extractKeyFromUrl(imageUrl);
            s3Operations.deleteObject(bucketName, key);
        } catch (Exception e) {
            log.error("Failed to delete image from S3: {}", imageUrl, e);
            throw new CustomException(GlobalErrorCode.SERVER_ERROR);
        }
    }

    private Image uploadOnS3(
            MultipartFile imageFile,
            String storeKey,
            ObjectMetadata metadata
    ) {
        try (InputStream inputStream = imageFile.getInputStream()) {
            S3Resource resource = s3Operations.upload(bucketName, storeKey, inputStream, metadata);
            return new Image(resource.getURL().toString());
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new CustomException(GlobalErrorCode.SERVER_ERROR);
        }
    }

    private String extractKeyFromUrl(String imageUrl) {
        return imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
    }
}
