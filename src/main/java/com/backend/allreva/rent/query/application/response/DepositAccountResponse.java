package com.backend.allreva.rent.query.application.response;

import com.backend.allreva.rent.command.domain.Rent;

public record DepositAccountResponse(String depositAccount) {
    public static DepositAccountResponse from(Rent rent) {
        return new DepositAccountResponse(rent.getDetailInfo().getDepositAccount());
    }
}
