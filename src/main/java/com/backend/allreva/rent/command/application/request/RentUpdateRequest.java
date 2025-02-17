package com.backend.allreva.rent.command.application.request;

import com.backend.allreva.common.model.Image;
import com.backend.allreva.rent.command.domain.value.BusSize;
import com.backend.allreva.rent.command.domain.value.BusType;
import com.backend.allreva.rent.command.domain.value.Region;
import com.backend.allreva.rent_join.command.domain.value.RefundType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.util.List;

public record RentUpdateRequest(
        @NotNull
        Long rentId,
        @NotNull
        Region region, // enum 파싱
        @NotNull
        String boardingArea,
        @NotNull
        String upTime,
        @NotNull
        String downTime,
        @NotEmpty(message = "날짜는 하루 이상 선택되어야 합니다.")
        @JsonProperty("boardingDates")
        List<LocalDate> rentBoardingDateRequests,
        @NotNull
        BusSize busSize, // enum
        @NotNull
        BusType busType, // enum
        @Min(value = 1, message = "탑승 인원 수는 1명 이상이어야 합니다.")
        int maxPassenger,
        @PositiveOrZero
        int roundPrice,
        @PositiveOrZero
        int upTimePrice,
        @PositiveOrZero
        int downTimePrice,
        @Min(value = 1, message = "모집 인원 수는 1명 이상이어야 합니다.")
        int recruitmentCount,
        @FutureOrPresent(message = "마감 기한은 과거일 수 없습니다.")
        LocalDate endDate,
        String chatUrl,
        @NotNull
        RefundType refundType, //enum
        String information,
        Image image
) {

}
