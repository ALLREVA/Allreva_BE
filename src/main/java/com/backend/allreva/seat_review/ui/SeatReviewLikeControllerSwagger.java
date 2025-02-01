package com.backend.allreva.seat_review.ui;

import com.backend.allreva.common.dto.Response;
import com.backend.allreva.member.command.domain.Member;
import com.backend.allreva.seat_review.command.application.dto.SeatReviewLikeRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "좌석리뷰 좋아요 API", description = "좌석리뷰 좋아요 API")
public interface SeatReviewLikeControllerSwagger {

    @Operation(summary = "좌석리뷰 좋아요 API", description = "좌석리뷰 좋아요 누르는 API")
    Response<Long> likeSeatReview(
            SeatReviewLikeRequest request,
            Member member
    );

    @Operation(summary = "좌석리뷰 좋아요 취소 API", description = "좌석리뷰 좋아요 취소 API")
    Response<Void> likeSeatReviewCancel(
            Long seatReviewLikeId,
            Member member
    );
}
