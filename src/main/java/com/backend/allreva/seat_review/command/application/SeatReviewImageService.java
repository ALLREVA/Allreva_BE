package com.backend.allreva.seat_review.command.application;

import com.backend.allreva.common.application.S3ImageService;
import com.backend.allreva.common.model.Image;
import com.backend.allreva.seat_review.command.application.dto.FileData;
import com.backend.allreva.seat_review.command.domain.SeatReviewImage;
import com.backend.allreva.seat_review.exception.SeatReviewImageSaveFailedException;
import com.backend.allreva.seat_review.infra.SeatReviewImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class SeatReviewImageService {

    private final SeatReviewImageRepository seatReviewImageRepository;
    private final S3ImageService s3ImageService;

    @Async("taskExecutor")
    public void uploadAndSaveImages(Long seatReviewId, List<MultipartFile> images) {
        List<Image> uploadedImages = new ArrayList<>();
        try {
            // MultipartFile 데이터를 미리 메모리에 로드
            List<FileData> fileDataList = prepareFileData(images);
            uploadedImages = uploadImages(fileDataList);
            saveSeatReviewImages(seatReviewId, uploadedImages);
            log.info("SeatReview 이미지 업로드 및 저장 완료! seatReviewId: {}", seatReviewId);
        } catch (Exception e) {
            rollbackImages(uploadedImages);
            log.error("이미지 업로드 중 오류 발생! seatReviewId: {}", seatReviewId, e);
            throw new SeatReviewImageSaveFailedException();
        }
    }

    @Async("taskExecutor")
    public void deleteImages(Long seatReviewId) {
        List<SeatReviewImage> images = seatReviewImageRepository.findBySeatReviewId(seatReviewId);

        if (images.isEmpty()) {
            log.info("삭제할 이미지가 없습니다. seatReviewId: {}", seatReviewId);
            return;
        }

        try {
            // S3에서 이미지 삭제
//            images.forEach(image -> s3ImageService.delete(image.getUrl()));
//            // DB에서 이미지 엔티티 삭제
            seatReviewImageRepository.deleteAll(images);
            log.info("SeatReview 이미지 삭제 완료! seatReviewId: {}", seatReviewId);
        } catch (Exception e) {
            log.error("이미지 삭제 중 오류 발생! seatReviewId: {}", seatReviewId, e);
            throw new RuntimeException("이미지 삭제 실패", e);
        }
    }

    private List<FileData> prepareFileData(List<MultipartFile> images) {
        return images.stream()
                .map(file -> {
                    try {
                        return new FileData(file.getBytes(), file.getOriginalFilename());
                    } catch (IOException e) {
                        throw new RuntimeException("파일 변환 실패", e);
                    }
                })
                .toList();
    }

    private List<Image> uploadImages(List<FileData> fileDataList) {
        List<CompletableFuture<Image>> futures = fileDataList.stream()
                .map(fileData -> uploadImageAsync(fileData))
                .toList();

        return futures.stream()
                .map(CompletableFuture::join)
                .toList();
    }

    private void saveSeatReviewImages(Long seatReviewId, List<Image> uploadedImages) {
        List<SeatReviewImage> seatReviewImages = new ArrayList<>();

        for (int i = 0; i < uploadedImages.size(); i++) {
            Image img = uploadedImages.get(i);
            SeatReviewImage seatReviewImage = SeatReviewImage.builder()
                    .url(img.getUrl())
                    .seatReviewId(seatReviewId)
                    .orderNum(i + 1) // 순서를 설정
                    .build();
            seatReviewImages.add(seatReviewImage);
        }

        seatReviewImageRepository.saveAll(seatReviewImages);
    }

    private void rollbackImages(List<Image> uploadedImages) {
        uploadedImages.forEach(image -> deleteImageAsync(image.getUrl()));
    }

    @Async("taskExecutor")
    public CompletableFuture<Image> uploadImageAsync(FileData fileData) {
        return CompletableFuture.completedFuture(s3ImageService.upload(fileData));
    }

    @Async("taskExecutor")
    public void deleteImageAsync(final String imageUrl) {
        s3ImageService.delete(imageUrl);
    }
}

