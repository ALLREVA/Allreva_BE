package com.backend.allreva.seat_review.infra;

import com.backend.allreva.seat_review.command.domain.SeatReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatReviewImageRepository extends JpaRepository<SeatReviewImage, Long> {
    List<SeatReviewImage> findBySeatReviewId(Long id);
}
