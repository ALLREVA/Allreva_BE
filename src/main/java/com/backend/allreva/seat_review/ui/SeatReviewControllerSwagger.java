package com.backend.allreva.seat_review.ui;

import com.backend.allreva.common.dto.Response;
import com.backend.allreva.member.command.domain.Member;
import com.backend.allreva.seat_review.command.application.dto.ReviewCreateRequest;
import com.backend.allreva.seat_review.command.application.dto.ReviewUpdateRequest;
import com.backend.allreva.seat_review.query.application.dto.SeatReviewResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "좌석리뷰 API", description = "좌석리뷰 API")
public interface SeatReviewControllerSwagger {

    @Operation(summary = "좌석리뷰 생성 API", description = "좌석리뷰 생성 API")
    Response<Long> createSeatReview(
            ReviewCreateRequest request,
            Member member
    );

    @Operation(summary = "좌석리뷰 수정 API", description = "좌석리뷰 수정 API")
    Response<Long> updateSeatReview(
            ReviewUpdateRequest request,
            Member member
    );

    @Operation(summary = "좌석리뷰 삭제 API", description = "좌석리뷰 삭제 API")
    Response<Void> deleteSeatReview(
            Long seatReviewId,
            Member member
    );

    @Operation(summary = "좌석리뷰 조회 API", description = "좌석리뷰 조회 API")
    Response<List<SeatReviewResponse>> getReviews(
            Long lastId,
            LocalDateTime lastCreatedAt,
            int size,
            SortType sortType,
            String hallId,
            Member member
    );
}
