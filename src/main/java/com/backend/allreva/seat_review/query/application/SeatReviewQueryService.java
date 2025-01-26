package com.backend.allreva.seat_review.query.application;

import com.backend.allreva.seat_review.infra.SeatReviewRepository;
import com.backend.allreva.seat_review.query.application.dto.SeatReviewResponse;
import com.backend.allreva.seat_review.ui.SeatReviewSearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SeatReviewQueryService {
    private final SeatReviewRepository seatReviewRepository;

    public List<SeatReviewResponse> getReviews(
            final SeatReviewSearchCondition condition,
            final Long currentMemberId) {
        return seatReviewRepository.findReviewsWithNoOffset(condition, currentMemberId);
    }


}

