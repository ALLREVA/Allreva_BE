package com.backend.allreva.rent.command.application.request;

import com.backend.allreva.common.model.Image;
import com.backend.allreva.rent.command.domain.Rent;
import com.backend.allreva.rent.command.domain.RentBoardingDate;
import com.backend.allreva.rent.command.domain.value.AdditionalInfo;
import com.backend.allreva.rent.command.domain.value.Bus;
import com.backend.allreva.rent.command.domain.value.BusSize;
import com.backend.allreva.rent.command.domain.value.BusType;
import com.backend.allreva.rent.command.domain.value.DetailInfo;
import com.backend.allreva.rent.command.domain.value.OperationInfo;
import com.backend.allreva.rent.command.domain.value.Price;
import com.backend.allreva.rent.command.domain.value.Region;
import com.backend.allreva.rent_join.command.domain.value.RefundType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.util.List;

public record RentRegisterRequest(
        @NotNull
        Long concertId,
        @NotBlank
        String title,
        @NotNull
        String artistName,
        @NotNull
        Region region, // enum 파싱
        @NotNull
        String depositAccount,
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
        RefundType refundType, // enum
        String information,
        Image image
) {

    public Rent toEntity(
            final Long memberId
    ) {
        Rent rent = Rent.builder()
                .memberId(memberId)
                .concertId(concertId)
                .boardingDates(rentBoardingDateRequests.stream()
                        .map(request -> RentBoardingDate.builder()
                                .date(request)
                                .build())
                        .toList())
                .detailInfo(DetailInfo.builder()
                        .image(image)
                        .title(title)
                        .artistName(artistName)
                        .depositAccount(depositAccount)
                        .region(region)
                        .build())
                .operationInfo(OperationInfo.builder()
                        .boardingArea(boardingArea)
                        .upTime(upTime)
                        .downTime(downTime)
                        .bus(Bus.builder()
                                .busSize(busSize)
                                .busType(busType)
                                .maxPassenger(maxPassenger)
                                .build())
                        .price(Price.builder()
                                .roundPrice(roundPrice)
                                .upTimePrice(upTimePrice)
                                .downTimePrice(downTimePrice)
                                .build())
                        .build())
                .additionalInfo(AdditionalInfo.builder()
                        .recruitmentCount(recruitmentCount)
                        .chatUrl(chatUrl)
                        .refundType(refundType)
                        .information(information)
                        .endDate(endDate)
                        .build())
                .build();

        List<RentBoardingDate> rentBoardingDates = rentBoardingDateRequests.stream()
                .map(date -> RentBoardingDate.builder()
                        .date(date)
                        .build())
                .toList();
        rent.assignBoardingDates(rentBoardingDates);
        return rent;
    }
}
