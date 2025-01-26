package com.backend.allreva.concert.fixture;

import com.backend.allreva.common.model.Image;
import com.backend.allreva.concert.command.domain.Concert;
import com.backend.allreva.concert.command.domain.value.Code;
import com.backend.allreva.concert.command.domain.value.ConcertInfo;
import com.backend.allreva.concert.command.domain.value.ConcertStatus;
import com.backend.allreva.concert.command.domain.value.DateInfo;
import com.backend.allreva.concert.command.domain.value.Seller;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class ConcertFixture {

    public static Concert createConcertFixture(final String hallCode) {
        return Concert.builder()
                .code(Code.builder()
                        .hallCode(hallCode)
                        .concertCode("concertCode")
                        .build())
                .concertInfo(ConcertInfo.builder()
                        .title("Sample Concert")
                        .price("2024-12-01")
                        .performStatus(ConcertStatus.IN_PROGRESS)
                        .host("host")
                        .dateInfo(DateInfo.builder()
                                .startDate(LocalDate.of(2024, 11, 30))
                                .endDate(LocalDate.of(2024, 12, 1))
                                .timeTable("timetable")
                                .build())
                        .build())
                .poster(new Image("http://example.com/poster.jpg"))
                .detailImages(List.of(new Image("http://example.com/detail1.jpg"), new Image("http://example.com/detail2.jpg")))
                .sellers(Set.of(Seller
                        .builder()
                        .name("Sample Seller")
                        .salesUrl("http://seller.com")
                        .build()))
                .build();
    }
}
