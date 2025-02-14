package com.backend.allreva.rent_join.exception;

import com.backend.allreva.common.exception.CustomException;
import com.backend.allreva.rent.exception.RentErrorCode;

public class PassengersMaximumReachedException extends CustomException {

    public PassengersMaximumReachedException() {
        super(RentErrorCode.PASSENGERS_MAXIMUM_REACHED);
    }
}
