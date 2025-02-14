package com.backend.allreva.rent_join.exception;

import com.backend.allreva.common.exception.CustomException;
import com.backend.allreva.rent.exception.RentErrorCode;

public class RentJoinAlreadyExistsException extends CustomException {

    public RentJoinAlreadyExistsException() {
        super(RentErrorCode.RENT_JOIN_ALREADY_EXISTS);
    }
}
