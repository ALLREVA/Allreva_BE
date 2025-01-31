package com.backend.allreva.seat_review.exception;

import com.backend.allreva.common.exception.CustomException;

public class DuplicateLikeException extends CustomException {
    public DuplicateLikeException() {
        super(SeatReviewErrorCode.DUPLICATE_LIKE);
    }
}
