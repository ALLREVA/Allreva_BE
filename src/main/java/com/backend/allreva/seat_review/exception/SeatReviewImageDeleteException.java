package com.backend.allreva.seat_review.exception;

import com.backend.allreva.common.exception.CustomException;

import static com.backend.allreva.seat_review.exception.SeatReviewErrorCode.SEAT_REVIEW_IMAGE_DELETE_FAILED;

public class SeatReviewImageDeleteException extends CustomException {
    public SeatReviewImageDeleteException() {
        super(SEAT_REVIEW_IMAGE_DELETE_FAILED);
    }
}
