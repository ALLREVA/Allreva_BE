package com.backend.allreva.seat_review.command.application;

import com.backend.allreva.member.command.domain.Member;
import com.backend.allreva.seat_review.command.application.dto.ReviewCreateRequest;
import com.backend.allreva.seat_review.command.application.dto.ReviewUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional
public class SeatReviewFacade {

    private final SeatReviewService seatReviewService;
    private final SeatReviewImageService seatReviewImageService;

    public Long createSeatReview(
            final ReviewCreateRequest request,
            final List<MultipartFile> images,
            final Member member
    ) {
        Long seatReviewId = seatReviewService.createSeatReview(request, member);

        // 비동기로 이미지 업로드 및 저장 처리
        seatReviewImageService.uploadAndSaveImages(seatReviewId, images);

        return seatReviewId;
    }

    public Long updateSeatReview(
            final ReviewUpdateRequest request,
            final List<MultipartFile> images,
            final Member member
    ) {
        Long updatedSeatReviewId = seatReviewService.updateSeatReview(request, member);

        // 기존 이미지 삭제 비동기 처리
        seatReviewImageService.deleteImages(request.seatReviewId());

        // 새로운 이미지 업로드 및 저장 비동기 처리
        seatReviewImageService.uploadAndSaveImages(request.seatReviewId(), images);

        return updatedSeatReviewId;
    }

    public void deleteSeatReview(
            final Long seatReviewId,
            final Member member
    ) {
        seatReviewImageService.deleteImages(seatReviewId);

        seatReviewService.deleteSeatReview(seatReviewId, member);
    }

}
