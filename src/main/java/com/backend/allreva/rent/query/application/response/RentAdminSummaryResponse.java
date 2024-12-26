package com.backend.allreva.rent.query.application.response;

import com.backend.allreva.rent.command.domain.value.BusSize;
import com.backend.allreva.rent.command.domain.value.BusType;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record RentAdminSummaryResponse(
        Long rentId,
        String title, // 차량 대절 제목
        LocalDate boardingDate, // 차량 대절 날짜
        String boardingArea, // TODO: boardingArea or Region?
        LocalDateTime rentStartDate, // 차량 대절 모집 시작 시간
        LocalDate rentEndDate, // 차량 대절 모집 종료 시간
        int recruitmentCount, // 차량 대절 모집 인원
        int participationCount, // 차량 대절 현재 참여 인원
        boolean isClosed, // 모집중 상태
        BusSize busSize, // 버스
        BusType busType, // 버스
        int maxPassenger // 버스
) {

}
