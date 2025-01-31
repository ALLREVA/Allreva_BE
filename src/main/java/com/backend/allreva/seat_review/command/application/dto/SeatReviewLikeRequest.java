package com.backend.allreva.seat_review.command.application.dto;

import jakarta.validation.constraints.NotNull;

public record SeatReviewLikeRequest(
        @NotNull(message = "seatReviewId는 필수 입니다")
        Long seatReviewId
) {
}
