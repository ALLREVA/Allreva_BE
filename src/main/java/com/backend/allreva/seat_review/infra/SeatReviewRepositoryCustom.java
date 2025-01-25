package com.backend.allreva.seat_review.infra;

import com.backend.allreva.seat_review.query.application.dto.SeatReviewResponse;
import com.backend.allreva.seat_review.ui.SeatReviewSearchCondition;

import java.util.List;

public interface SeatReviewRepositoryCustom {
    List<SeatReviewResponse> findReviewsWithNoOffset(SeatReviewSearchCondition condition, Long currentMemberId);
}
