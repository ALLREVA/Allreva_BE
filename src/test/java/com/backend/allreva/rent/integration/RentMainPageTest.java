package com.backend.allreva.rent.integration;

import static com.backend.allreva.concert.fixture.ConcertFixture.createConcertFixture;
import static com.backend.allreva.concert.fixture.ConcertHallFixture.createConcertHallFixture;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.backend.allreva.common.model.Image;
import com.backend.allreva.concert.command.domain.ConcertRepository;
import com.backend.allreva.hall.command.domain.ConcertHallRepository;
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
import com.backend.allreva.rent.query.application.RentQueryService;
import com.backend.allreva.rent_join.command.domain.RentJoin;
import com.backend.allreva.rent_join.command.domain.RentJoinRepository;
import com.backend.allreva.rent_join.command.domain.value.BoardingType;
import com.backend.allreva.rent_join.command.domain.value.Depositor;
import com.backend.allreva.rent_join.command.domain.value.RefundType;
import com.backend.allreva.support.IntegrationTestSupport;
import com.backend.allreva.survey.query.application.response.SortType;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("NonAsciiCharacters")
@Transactional
class RentMainPageTest extends IntegrationTestSupport {

    @Autowired
    private RentQueryService rentQueryService;
    @Autowired
    private RentJoinRepository rentJoinRepository;
    @Autowired
    private RentRepository rentRepository;
    @Autowired
    private ConcertHallRepository concertHallRepository;
    @Autowired
    private ConcertRepository concertRepository;

    @Test
    void 차량_대절_리스트를_지역별로_조회한다() {
        // given
        var registerId = 1L;
        var savedRent1 = rentRepository.save(createRentFixture(
                registerId, 1L, Region.서울, LocalDate.of(2024, 9, 20)));
        rentRepository.save(createRentFixture(
                registerId, 2L, Region.경기, LocalDate.of(2024, 9, 21)));

        // when
        var rentSummaries = rentQueryService.getRentSummaries(
                Region.서울, SortType.LATEST, null, null, 10);

        // then
        assertThat(rentSummaries).hasSize(1);
        assertThat(rentSummaries.get(0).rentId()).isEqualTo(savedRent1.getId());
    }

    @Test
    void 차량_대절_리스트를_마감순으로_조회한다() {
        // given
        var registerId = 1L;
        var savedRent1 = rentRepository.save(createRentFixture(
                registerId, 1L, Region.서울, LocalDate.of(2024, 9, 21)));
        var savedRent2 = rentRepository.save(createRentFixture(
                registerId, 2L, Region.경기, LocalDate.of(2024, 9, 20)));

        // when
        var rentSummaries = rentQueryService.getRentSummaries(
                null, SortType.CLOSING, null, null, 10);

        // then
        assertThat(rentSummaries).hasSize(2);
        assertThat(rentSummaries.get(0).rentId()).isEqualTo(savedRent2.getId());
        assertThat(rentSummaries.get(1).rentId()).isEqualTo(savedRent1.getId());
    }

    @Test
    void 차량_대절_폼_상세_조회를_성공한다() {
        // given
        var registerId = 1L;
        var user2Id = 2L;
        var user3Id = 3L;
        var concertHall = concertHallRepository.save(createConcertHallFixture());
        var concert = concertRepository.save(createConcertFixture(concertHall.getId()));
        var savedRent = rentRepository.save(createRentFixture(registerId, concert.getId(), Region.서울, LocalDate.of(2024, 9, 21)));
        var boardingDates = savedRent.getBoardingDates();
        rentJoinRepository.save(createRentJoinFixture(savedRent.getId(), user2Id, "홍길동", boardingDates.get(0).getDate()));
        rentJoinRepository.save(createRentJoinFixture(savedRent.getId(), user3Id, "김철수", boardingDates.get(1).getDate()));

        // when
        var rentDetail = rentQueryService.getRentDetailById(savedRent.getId());

        // then
        assertThat(rentDetail).isNotNull();
        assertSoftly(softly -> {
            softly.assertThat(rentDetail.title()).isEqualTo(savedRent.getDetailInfo().getTitle());
            softly.assertThat(rentDetail.concertName()).isEqualTo(concert.getConcertInfo().getTitle());
            softly.assertThat(rentDetail.dropOffArea()).isEqualTo(concertHall.getName());
            softly.assertThat(rentDetail.boardingDates().get(0).participationCount()).isEqualTo(2);
        });
    }

    @Test
    void 입금_계좌_조회를_성공한다() {
        // given
        var registerId = 1L;
        var savedRent = rentRepository.save(createRentFixture(
                registerId, 1L, Region.서울, LocalDate.of(2024, 9, 21)));

        // when
        var depositAccount = rentQueryService.getDepositAccountById(savedRent.getId());

        // then
        assertThat(depositAccount).isNotNull();
        assertThat(depositAccount.depositAccount()).isEqualTo("depositAccount");
    }

    private Rent createRentFixture(
            final Long memberId,
            final Long concertId,
            final Region region,
            final LocalDate endDate
    ) {
        Rent rent = Rent.builder()
                .memberId(memberId)
                .concertId(concertId)
                .detailInfo(DetailInfo.builder()
                        .image(new Image("imageUrl"))
                        .title("title")
                        .artistName("artistName")
                        .depositAccount("depositAccount")
                        .region(region)
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
                        .endDate(endDate)
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
