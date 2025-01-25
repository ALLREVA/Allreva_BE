package com.backend.allreva.seat_review.exception;

import com.backend.allreva.common.exception.CustomException;

public class NotWriterException extends CustomException {
    public NotWriterException() {
        super(SeatReviewErrorCode.NOT_WRITER);
    }
}
