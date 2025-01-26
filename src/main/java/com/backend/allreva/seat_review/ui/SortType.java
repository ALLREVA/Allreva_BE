package com.backend.allreva.seat_review.ui;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SortType {
    CREATED_AT("생성일순"),
    LIKE_COUNT("좋아요순");

    private final String description;
}