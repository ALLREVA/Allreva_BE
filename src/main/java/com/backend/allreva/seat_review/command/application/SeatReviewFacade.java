package com.backend.allreva.seat_review.command.application;

import com.backend.allreva.member.command.domain.Member;
import com.backend.allreva.seat_review.command.application.dto.ReviewCreateRequest;
import com.backend.allreva.seat_review.command.application.dto.ReviewUpdateRequest;
import com.backend.allreva.seat_review.command.domain.SeatReview;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class SeatReviewFacade {

    private final SeatReviewService seatReviewService;
    private final SeatReviewImageService seatReviewImageService;

    public Long createSeatReview(
            final ReviewCreateRequest request,
            final Member member
    ) {
        Long seatReviewId = seatReviewService.createSeatReview(request, member);
        // 이미지 url 저장
        seatReviewImageService.uploadAndSaveImages(seatReviewId,request.imageUrls());

        return seatReviewId;
    }

    public Long updateSeatReview(
            final ReviewUpdateRequest request,
            final Member member
    ) {
        SeatReview seatReview = seatReviewService.updateSeatReview(request, member);

        // 기존 이미지 삭제
        seatReviewImageService.deleteImages(request.seatReviewId());

        // 새로운 이미지 업로드
        seatReviewImageService.uploadAndSaveImages(request.seatReviewId(), request.imageUrls());

        return seatReview.getId();
    }

    public void deleteSeatReview(
            final Long seatReviewId,
            final Member member
    ) {
        seatReviewImageService.deleteImages(seatReviewId);

        seatReviewService.deleteSeatReview(seatReviewId, member);
    }

}
