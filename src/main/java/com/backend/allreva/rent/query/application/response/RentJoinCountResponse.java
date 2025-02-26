package com.backend.allreva.rent.query.application.response;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record RentJoinCountResponse(
        int rentUpCount, // 이용 구분 - 상행 참여 인원
        int rentDownCount, // 이용 구분 - 하행 참여 인원
        int rentRoundCount, // 이용 구분 - 왕복 참여 인원
        int refundCount, // 입금 처리 - 환불 인원
        int additionalDepositCount // 입금 처리 - 추가 입금 인원
) {
    public static final RentJoinCountResponse EMPTY = RentJoinCountResponse.builder().build();
}
