package com.backend.allreva.seat_review.exception;

import com.backend.allreva.common.exception.CustomException;

public class SeatReviewImageSaveFailedException extends CustomException {
    public SeatReviewImageSaveFailedException() {
        super(SeatReviewErrorCode.SEAT_REVIEW_IMAGE_SAVE_FAILED);
    }
}
