package com.backend.allreva.seat_review.infra;

import com.backend.allreva.seat_review.command.domain.SeatReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatReviewLikeRepository extends JpaRepository<SeatReviewLike, Long> {
    boolean existsByReviewIdAndMemberId(Long seatReviewId, Long memberId);
}
