package com.backend.allreva.seat_review.exception;

import com.backend.allreva.common.exception.CustomException;

public class SeatReviewSaveFailedException extends CustomException {
    public SeatReviewSaveFailedException() {
        super(SeatReviewErrorCode.SEAT_REVIEW_SAVE_FAILED);
    }
}
