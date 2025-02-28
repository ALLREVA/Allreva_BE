package com.backend.allreva.rent.query;

import static com.backend.allreva.rent.fixture.RentFixture.createRentFixture;
import static com.backend.allreva.rent.fixture.RentJoinFixture.createRentJoinFixture;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.backend.allreva.rent.command.domain.RentRepository;
import com.backend.allreva.rent.query.application.RentQueryService;
import com.backend.allreva.rent_join.command.domain.RentJoinRepository;
import com.backend.allreva.support.IntegrationTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("NonAsciiCharacters")
@Transactional
class RentAdminPageTest extends IntegrationTestSupport {

    @Autowired
    private RentQueryService rentQueryService;
    @Autowired
    private RentRepository rentRepository;
    @Autowired
    private RentJoinRepository rentJoinRepository;

    @Test
    void 내가_등록한_차량_대절_리스트를_조회한다() {
        // given
        var registerId = 1L;
        var rent = rentRepository.save(createRentFixture(registerId, 1L));

        var userA = 2L;
        var userB = 3L;
        var rentJoinByUserA = rentJoinRepository.save(createRentJoinFixture(rent.getId(), userA));
        var rentJoinByUserB = rentJoinRepository.save(createRentJoinFixture(rent.getId(), userB));

        // when
        var rentAdminSummaries = rentQueryService.getRentAdminSummariesByMemberId(registerId);

        // then
        assertThat(rentAdminSummaries).hasSize(1);
        assertSoftly(softly -> {
            // 총 모집 인원 테스트
            var recruitmentCountByQuery = rentAdminSummaries.get(0).recruitmentCount();
            var recruitmentCount = rent.getAdditionalInfo().getRecruitmentCount();
            softly.assertThat(recruitmentCountByQuery).isEqualTo(recruitmentCount);
            // 현재 모집 인원 테스트
            var participationCountByQuery = rentAdminSummaries.get(0).participationCount();
            var participationCount = rentJoinByUserA.getPassengerNum() + rentJoinByUserB.getPassengerNum();
            softly.assertThat(participationCountByQuery).isEqualTo(participationCount);
        });
    }

    @Test
    void 차량_대절_관리_페이지에서_내가_등록한_차량_대절_상세_정보를_조회한다() {
        // given
        var registerId = 1L;
        var rent = rentRepository.save(createRentFixture(registerId, 1L));

        var userA = 2L;
        var rentJoinByUserA = rentJoinRepository.save(createRentJoinFixture(rent.getId(), userA));

        // when
        var rentAdminDetail = rentQueryService.getRentAdminDetail(registerId, rentJoinByUserA.getBoardingDate(), rentJoinByUserA.getRentId());

        // then
        assertThat(rentAdminDetail).isNotNull();
        assertSoftly(softly -> {
            softly.assertThat(rentAdminDetail.getRentJoinCountResponse().rentRoundCount()).isEqualTo(1);
            softly.assertThat(rentAdminDetail.getRentJoinCountResponse().additionalDepositCount()).isEqualTo(1);
            softly.assertThat(rentAdminDetail.getRentJoinDetailResponses().get(0).rentJoinId()).isEqualTo(rentJoinByUserA.getId());
        });
    }
}
