package com.backend.allreva.seat_review.command.application;

import com.backend.allreva.common.application.S3ImageService;
import com.backend.allreva.common.model.Image;
import com.backend.allreva.seat_review.command.domain.SeatReviewImage;
import com.backend.allreva.seat_review.exception.SeatReviewImageDeleteException;
import com.backend.allreva.seat_review.exception.SeatReviewImageSaveFailedException;
import com.backend.allreva.seat_review.infra.SeatReviewImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SeatReviewImageService {

    private final SeatReviewImageRepository seatReviewImageRepository;
    private final S3ImageService s3ImageService;


    public void uploadAndSaveImages(Long seatReviewId, List<Image> imageUrls) {
        try {
            saveSeatReviewImages(seatReviewId, imageUrls);
        } catch (Exception e) {
            throw new SeatReviewImageSaveFailedException();
        }
    }


    public void deleteImages(Long seatReviewId) {
        List<SeatReviewImage> images = seatReviewImageRepository.findBySeatReviewId(seatReviewId);
        log.info("images : {}", images.toString());
        if (images.isEmpty()) {
            return;
        }

        try {
            seatReviewImageRepository.deleteAll(images);
        } catch (Exception e) {
            throw new SeatReviewImageDeleteException();
        }
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
}

