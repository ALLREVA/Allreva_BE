package com.backend.allreva.seat_review.exception;

import com.backend.allreva.common.exception.CustomException;

public class SeatReviewNotFoundException extends CustomException {
    public SeatReviewNotFoundException() {
        super(SeatReviewErrorCode.SEAT_REVIEW_NOT_FOUND);
    }
}
