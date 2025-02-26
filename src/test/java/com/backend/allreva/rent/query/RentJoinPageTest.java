package com.backend.allreva.rent.query;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.backend.allreva.common.model.Image;
import com.backend.allreva.rent.command.domain.Rent;
import com.backend.allreva.rent.command.domain.RentBoardingDate;
import com.backend.allreva.rent.command.domain.RentRepository;
import com.backend.allreva.rent.command.domain.value.AdditionalInfo;
import com.backend.allreva.rent.command.domain.value.Bus;
import com.backend.allreva.rent.command.domain.value.BusSize;
import com.backend.allreva.rent.command.domain.value.BusType;
import com.backend.allreva.rent.command.domain.value.DetailInfo;
import com.backend.allreva.rent.command.domain.value.OperationInfo;
import com.backend.allreva.rent.command.domain.value.Price;
import com.backend.allreva.rent.command.domain.value.Region;
import com.backend.allreva.rent_join.command.domain.RentJoin;
import com.backend.allreva.rent_join.command.domain.RentJoinRepository;
import com.backend.allreva.rent_join.command.domain.value.BoardingType;
import com.backend.allreva.rent_join.command.domain.value.Depositor;
import com.backend.allreva.rent_join.command.domain.value.RefundType;
import com.backend.allreva.rent_join.query.RentJoinQueryService;
import com.backend.allreva.support.IntegrationTestSupport;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("NonAsciiCharacters")
@Transactional
class RentJoinPageTest extends IntegrationTestSupport {

    @Autowired
    private RentJoinQueryService rentJoinQueryService;
    @Autowired
    private RentRepository rentRepository;
    @Autowired
    private RentJoinRepository rentJoinRepository;

    @Test
    void 자신이_참여중인_차량_대절_리스트를_조회한다() {
        // given
        var registerId = 1L;
        var rent = rentRepository.save(createRentFixture(registerId, 1L));

        var userA = 2L;
        var userB = 3L;
        var rentJoinByUserA = rentJoinRepository.save(createRentJoinFixture(rent.getId(), userA, "userA", rent.getBoardingDates().get(0).getDate()));
        rentJoinRepository.save(createRentJoinFixture(rent.getId(), userB, "userB", rent.getBoardingDates().get(1).getDate()));

        // when
        var rentJoinSummaries = rentJoinQueryService.getRentJoin(userA);

        // then
        assertThat(rentJoinSummaries).hasSize(1);
        assertThat(rentJoinSummaries.get(0).rentJoinId()).isEqualTo(rentJoinByUserA.getId());
    }

    private Rent createRentFixture(Long memberId, Long concertId) {
        Rent rent = Rent.builder()
                .memberId(memberId)
                .concertId(concertId)
                .detailInfo(DetailInfo.builder()
                        .image(new Image("imageUrl"))
                        .title("title")
                        .artistName("artistName")
                        .depositAccount("depositAccount")
                        .region(Region.서울)
                        .build())
                .operationInfo(OperationInfo.builder()
                        .boardingArea("boardingArea")
                        .upTime("09:00")
                        .downTime("23:00")
                        .bus(Bus.builder()
                                .busSize(BusSize.LARGE)
                                .busType(BusType.STANDARD)
                                .maxPassenger(28)
                                .build())
                        .price(Price.builder()
                                .roundPrice(30000)
                                .upTimePrice(30000)
                                .downTimePrice(30000)
                                .build())
                        .build())
                .additionalInfo(AdditionalInfo.builder()
                        .recruitmentCount(30)
                        .chatUrl("chatUrl")
                        .refundType(RefundType.BOTH)
                        .information("information")
                        .endDate(LocalDate.of(2024, 9, 13))
                        .build())
                .build();
        rent.assignBoardingDates(List.of(
                RentBoardingDate.builder()
                        .rent(rent)
                        .date(LocalDate.of(2024, 9, 20))
                        .build(),
                RentBoardingDate.builder()
                        .rent(rent)
                        .date(LocalDate.of(2024, 9, 21))
                        .build()));
        return rent;
    }

    private RentJoin createRentJoinFixture(
            final Long rentId,
            final Long memberId,
            final String depositorName,
            final LocalDate boardingDate
    ) {
        return RentJoin.builder()
                .rentId(rentId)
                .memberId(memberId)
                .depositor(Depositor.builder()
                        .depositorName(depositorName)
                        .depositorTime("21:30")
                        .phone("010-1234-5678")
                        .build())
                .passengerNum(2) // 탑승자
                .boardingType(BoardingType.ROUND)
                .refundType(RefundType.BOTH)
                .refundAccount("123-4567-4344-23")
                .boardingDate(boardingDate)
                .build();
    }
}
