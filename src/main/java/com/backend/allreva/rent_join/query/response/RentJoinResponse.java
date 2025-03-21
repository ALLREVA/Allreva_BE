package com.backend.allreva.rent_join.query.response;

import com.backend.allreva.rent_join.command.domain.value.BoardingType;
import com.backend.allreva.rent_join.command.domain.value.RefundType;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 자신이 참여한 차량 대절 마이페이지
 */
public record RentJoinResponse(
        Long rentId, // 차량 대절 ID
        String title, // 차량 대절 제목
        LocalDate rentBoardingDate, // 공연일(차 대절 가용 날짜)
        String boardingArea,
        LocalDateTime rentStartDate, // 차량 대절 모집 시작 시간
        LocalDate rentEndDate, // 차량 대절 모집 종료 시간
        int recruitmentCount, // 최대 모집 인원
        int participateCount, // 현재 모집 인원
        boolean isClosed, // 모집중 상태
        Long rentJoinId, // 차량 대절 참여 ID
        LocalDateTime applyDate, // 신청 날짜
        int passengerNum, // 탑승 인원
        BoardingType boardingType, // 이용 편도
        String depositorName, // 입금자명
        String depositorTime, // 입금 시각
        RefundType refundType // 환불 정책
) {

}
