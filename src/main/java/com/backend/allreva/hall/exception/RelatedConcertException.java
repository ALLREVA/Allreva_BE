package com.backend.allreva.hall.exception;

import com.backend.allreva.common.exception.CustomException;

import static com.backend.allreva.hall.exception.ConcertHallErrorCode.RELATED_CONCERT_EXCEPTION;

public class RelatedConcertException extends CustomException {
  public RelatedConcertException() {
    super(RELATED_CONCERT_EXCEPTION);
  }
}
