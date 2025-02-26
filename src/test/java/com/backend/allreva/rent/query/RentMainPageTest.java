package com.backend.allreva.rent.query;

import static com.backend.allreva.concert.fixture.ConcertFixture.createConcertFixture;
import static com.backend.allreva.concert.fixture.ConcertHallFixture.createConcertHallFixture;
import static com.backend.allreva.member.fixture.MemberFixture.createMemberFixture;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.backend.allreva.common.model.Image;
import com.backend.allreva.concert.command.domain.ConcertRepository;
import com.backend.allreva.hall.command.domain.ConcertHallRepository;
import com.backend.allreva.member.command.domain.value.MemberRole;
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
    private RentRepository rentRepository;
    @Autowired
    private RentJoinRepository rentJoinRepository;
    @Autowired
    private ConcertHallRepository concertHallRepository;
    @Autowired
    private ConcertRepository concertRepository;

    @Test
    void 차량_대절_리스트를_지역별로_조회한다() {
        // given
        var registerId = 1L;
        var rent1 = rentRepository.save(createRentFixture(registerId, 1L, Region.서울, LocalDate.of(2024, 9, 20)));
        rentRepository.save(createRentFixture(registerId, 2L, Region.경기, LocalDate.of(2024, 9, 21)));

        // when
        var rentSummaries = rentQueryService.getRentSummaries(Region.서울, SortType.LATEST, null, null, 10);

        // then
        assertThat(rentSummaries).hasSize(1);
        assertThat(rentSummaries.get(0).rentId()).isEqualTo(rent1.getId());
    }

    @Test
    void 차량_대절_리스트를_마감순으로_조회한다() {
        // given
        var registerId = 1L;
        var rent1 = rentRepository.save(createRentFixture(registerId, 1L, Region.서울, LocalDate.of(2024, 9, 20)));
        var rent2 = rentRepository.save(createRentFixture(registerId, 2L, Region.경기, LocalDate.of(2024, 9, 21)));

        // when
        var rentSummaries = rentQueryService.getRentSummaries(null, SortType.CLOSING, null, null, 10);

        // then
        assertThat(rentSummaries).hasSize(2);
        assertSoftly(softly -> {
            softly.assertThat(rentSummaries.get(0).rentId()).isEqualTo(rent1.getId());
            softly.assertThat(rentSummaries.get(1).rentId()).isEqualTo(rent2.getId());
        });
    }

    @Test
    void 차량_대절_폼_상세_조회를_성공한다() {
        // given
        var concertHall = concertHallRepository.save(createConcertHallFixture());
        var concert = concertRepository.save(createConcertFixture(concertHall.getId()));

        var registerId = 1L;
        var register = createMemberFixture(registerId, MemberRole.USER);
        var rent = rentRepository.save(createRentFixture(registerId, concert.getId(), Region.서울, LocalDate.of(2024, 9, 21)));

        var userA = 2L;
        var userB = 3L;
        rentJoinRepository.save(createRentJoinFixture(rent.getId(), userA, "홍길동", rent.getBoardingDates().get(0).getDate()));
        rentJoinRepository.save(createRentJoinFixture(rent.getId(), userB, "김철수", rent.getBoardingDates().get(1).getDate()));

        // when
        var rentDetail = rentQueryService.getRentDetailById(rent.getId(), register);

        // then
        assertThat(rentDetail).isNotNull();
        assertSoftly(softly -> {
            softly.assertThat(rentDetail.getTitle()).isEqualTo(rent.getDetailInfo().getTitle());
            softly.assertThat(rentDetail.getConcertName()).isEqualTo(concert.getConcertInfo().getTitle());
            softly.assertThat(rentDetail.getDropOffArea()).isEqualTo(concertHall.getName());
            softly.assertThat(rentDetail.getBoardingDates().get(0).getParticipationCount()).isEqualTo(2);
            softly.assertThat(rentDetail.getBoardingDates().get(0).getIsApplied()).isFalse();
            softly.assertThat(rentDetail.getRefundAccount()).isEqualTo(register.getRefundAccount());
        });
    }

    @Test
    void 차량_대절_폼_상세_조회할_때_익명_사용자면_신청_여부와_환불_계좌를_제공하지_않는다() {
        // given
        var registerId = 1L;
        var concertHall = concertHallRepository.save(createConcertHallFixture());
        var concert = concertRepository.save(createConcertFixture(concertHall.getId()));
        var rent = rentRepository.save(createRentFixture(registerId, concert.getId(), Region.서울, LocalDate.of(2024, 9, 21)));

        // when
        var rentDetail = rentQueryService.getRentDetailById(rent.getId(), null);

        // then
        assertThat(rentDetail).isNotNull();
        assertSoftly(softly -> {
            softly.assertThat(rentDetail.getBoardingDates().get(0).getIsApplied()).isNull();
            softly.assertThat(rentDetail.getRefundAccount()).isNull();
        });
    }

    @Test
    void 입금_계좌_조회를_성공한다() {
        // given
        var registerId = 1L;
        var rent = rentRepository.save(createRentFixture(registerId, 1L, Region.서울, LocalDate.of(2024, 9, 21)));

        // when
        var depositAccount = rentQueryService.getDepositAccountById(rent.getId());

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
