package com.backend.allreva.seat_review.exception;

import com.backend.allreva.common.exception.CustomException;

public class NotLikeMemberException extends CustomException {
    public NotLikeMemberException() {
      super(SeatReviewErrorCode.NOT_LIKE_MEMBER);
    }
}
