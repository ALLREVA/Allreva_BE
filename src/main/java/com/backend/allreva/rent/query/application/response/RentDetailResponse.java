package com.backend.allreva.rent.query.application.response;

import com.backend.allreva.member.command.domain.value.RefundAccount;
import com.backend.allreva.rent.command.domain.value.BusSize;
import com.backend.allreva.rent.command.domain.value.BusType;
import com.backend.allreva.rent.command.domain.value.Region;
import com.backend.allreva.rent_join.command.domain.value.RefundType;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
public class RentDetailResponse {

    private final String concertName; // 콘서트 이름
    private final String imageUrl; // 콘서트 이미지
    private final String title; // 차량 대절 이름
    private final String artistName; // 아티스트 이름
    private final Region region; // 차량 대절 지역
    private final String boardingArea; // 상행 지역
    private final String dropOffArea; // 하행 지역 -> concerthall code 조인
    private final String upTime; // 상행 시간
    private final String downTime; // 하행 시간
    @Setter
    private List<RentBoardingDateResponse> boardingDates;
    private final BusSize busSize; // 차량 정보 - 사이즈
    private final BusType busType; // 차량 정보 - 버스 타입
    private final int maxPassenger; // 차량 정보 - 최대 탑승 인원
    private final int roundPrice; // 왕복 가격
    private final int upTimePrice; // 상행 가격
    private final int downTimePrice; // 하행 가격
    private final int recruitmentCount; // 모집 인원
    private final LocalDate endDate; // 마감일
    private final String chatUrl; // 채팅 url - 지금은 필요없을듯 일단은 넣어놓자.
    private final RefundType refundType; //
    private final String information; // 기타 안내 사항
    private final boolean isClosed; // 마감 여부
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private RefundAccount refundAccount; // 환불 계좌

    public RentDetailResponse(
            final String concertName,
            final String imageUrl,
            final String title,
            final String artistName,
            final Region region,
            final String boardingArea,
            final String dropOffArea,
            final String upTime,
            final String downTime,
            final List<RentBoardingDateResponse> boardingDates,
            final BusSize busSize,
            final BusType busType,
            final int maxPassenger,
            final int roundPrice,
            final int upTimePrice,
            final int downTimePrice,
            final int recruitmentCount,
            final LocalDate endDate,
            final String chatUrl,
            final RefundType refundType,
            final String information,
            final boolean isClosed
    ) {
        this.concertName = concertName;
        this.imageUrl = imageUrl;
        this.title = title;
        this.artistName = artistName;
        this.region = region;
        this.boardingArea = boardingArea;
        this.dropOffArea = dropOffArea;
        this.upTime = upTime;
        this.downTime = downTime;
        this.boardingDates = boardingDates;
        this.busSize = busSize;
        this.busType = busType;
        this.maxPassenger = maxPassenger;
        this.roundPrice = roundPrice;
        this.upTimePrice = upTimePrice;
        this.downTimePrice = downTimePrice;
        this.recruitmentCount = recruitmentCount;
        this.endDate = endDate;
        this.chatUrl = chatUrl;
        this.refundType = refundType;
        this.information = information;
        this.isClosed = isClosed;
    }

    @Getter
    public static class RentBoardingDateResponse {
        private final LocalDate date;
        private final int participationCount;
        @Setter
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Boolean isApplied;

        public RentBoardingDateResponse(
                final LocalDate date,
                final int participationCount
        ) {
            this.date = date;
            this.participationCount = participationCount;
        }
    }
}
