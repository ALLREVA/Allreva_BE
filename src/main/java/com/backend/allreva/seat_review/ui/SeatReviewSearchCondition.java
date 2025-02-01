package com.backend.allreva.seat_review.ui;

import java.time.LocalDateTime;

public record SeatReviewSearchCondition(
        Long lastId,
        LocalDateTime lastCreatedAt,
        int size,
        SortType sortType,
        String hallId
) {}