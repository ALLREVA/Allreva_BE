package com.backend.allreva.hall.exception;

import com.backend.allreva.common.exception.CustomException;

public class ConcertHallNotFoundException extends CustomException {
    public ConcertHallNotFoundException() {
        super(ConcertHallErrorCode.CONCERT_HALL_SEARCH_NOTFOUND);
    }
}
