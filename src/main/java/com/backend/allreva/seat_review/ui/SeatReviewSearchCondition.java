package com.backend.allreva.seat_review.ui;

public record SeatReviewSearchCondition(
        Long lastId,
        int size,
        SortType sortType,
        String hallId
) {}