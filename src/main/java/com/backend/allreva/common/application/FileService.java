package com.backend.allreva.common.application;

import com.backend.allreva.common.application.dto.GetPresignedUrlRequest;
import com.backend.allreva.common.application.exception.FailToMakeUrlException;
import com.backend.allreva.common.application.exception.InvalidUrlException;
import com.backend.allreva.member.command.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.DeleteObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedDeleteObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {
    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;
    private final S3Presigner s3Presigner;

    public String getPreSignedUrl(
            final GetPresignedUrlRequest request,
            final Member member
    ) {
        String objectKey =
                member.getMemberInfo().getNickname() + "/"
                        + request.fileType().toString() + "/"
                        + UUID.randomUUID().toString() + "_" + request.fileName();
        objectKey = objectKey.replaceAll("-", "");

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(objectKey)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))  // v3에서 Duration 사용
                .putObjectRequest(objectRequest)
                .build();

        try {
            PresignedPutObjectRequest presignedRequest =
                    s3Presigner.presignPutObject(presignRequest);
            return presignedRequest.url().toString();
        }catch (Exception e) {
            throw new FailToMakeUrlException();
        }
    }

    public String getDeletePreSignedUrl(final String fileUrl) {
        // S3 객체 키 추출 (버킷 URL 이후의 경로)
        String objectKey = extractObjectKeyFromUrl(fileUrl);

        // DeleteObjectRequest 생성
        DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(objectKey)
                .build();

        // Presigned Delete Request 생성
        DeleteObjectPresignRequest presignDeleteRequest = DeleteObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10)) // 유효 시간 설정
                .deleteObjectRequest(deleteRequest)
                .build();
        try {
            PresignedDeleteObjectRequest presignedDeleteRequest =
                    s3Presigner.presignDeleteObject(presignDeleteRequest);

            return presignedDeleteRequest.url().toString();
        }catch (Exception e) {
            throw new InvalidUrlException();
        }

    }

    private String extractObjectKeyFromUrl(String fileUrl) {
        // 버킷 URL의 기본 형식: https://<bucket-name>.s3.<region>.amazonaws.com/<object-key>
        String bucketUrl = "https://" + bucket + ".s3.us-east-2.amazonaws.com/";
        if (fileUrl.startsWith(bucketUrl)) {
            return fileUrl.substring(bucketUrl.length());
        } else {
            throw new IllegalArgumentException("Invalid file URL: " + fileUrl);
        }
    }

}
