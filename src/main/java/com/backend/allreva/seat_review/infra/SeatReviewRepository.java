package com.backend.allreva.seat_review.infra;

import com.backend.allreva.seat_review.command.domain.SeatReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatReviewRepository extends
        JpaRepository<SeatReview, Long> , SeatReviewRepositoryCustom{
}
