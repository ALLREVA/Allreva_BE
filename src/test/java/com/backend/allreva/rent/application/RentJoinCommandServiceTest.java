package com.backend.allreva.rent.application;

import static com.backend.allreva.support.FixtureUtil.fixtureMonkey;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.backend.allreva.rent.command.domain.Rent;
import com.backend.allreva.rent.command.domain.RentRepository;
import com.backend.allreva.rent.fake.RentFakeRepository;
import com.backend.allreva.rent.fake.RentJoinFakeRepository;
import com.backend.allreva.rent_join.command.application.RentJoinCommandService;
import com.backend.allreva.rent_join.command.application.request.RentJoinApplyRequest;
import com.backend.allreva.rent_join.command.application.request.RentJoinIdRequest;
import com.backend.allreva.rent_join.command.application.request.RentJoinUpdateRequest;
import com.backend.allreva.rent_join.command.domain.RentJoin;
import com.backend.allreva.rent_join.command.domain.RentJoinRepository;
import com.backend.allreva.rent_join.exception.RentJoinAccessDeniedException;
import com.backend.allreva.rent_join.exception.RentJoinNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
@SuppressWarnings("NonAsciiCharacters")
class RentJoinCommandServiceTest {

    private final RentJoinCommandService rentJoinCommandService;
    private final RentRepository rentRepository;
    private final RentJoinRepository rentJoinRepository;

    public RentJoinCommandServiceTest() {
        this.rentRepository = new RentFakeRepository();
        this.rentJoinRepository = new RentJoinFakeRepository();
        this.rentJoinCommandService = new RentJoinCommandService(rentRepository, rentJoinRepository);
    }

    @Test
    void 차량_대절_신청_폼_지원에_성공한다() {
        // given
        var memberId = 1L;
        var rent = fixtureMonkey.giveMeBuilder(Rent.class)
                .setNull("id")
                .set("memberId", memberId)
                .set("additionalInfo.recruitmentCount", 20)
                .sample();
        rentRepository.save(rent);

        var rentJoinApplyRequest = fixtureMonkey.giveMeBuilder(RentJoinApplyRequest.class)
                .set("rentId", rent.getId())
                .set("passengerNum", 5)
                .sample();

        // when
        var appliedRentId = rentJoinCommandService.applyRent(rentJoinApplyRequest, memberId);

        // then
        rentJoinRepository.findById(appliedRentId).ifPresent(rentJoin -> {
            assertSoftly(softly -> {
                softly.assertThat(rentJoin.getMemberId()).isEqualTo(memberId);
                softly.assertThat(rentJoin.getRentId()).isEqualTo(rent.getId());
            });
        });
    }

    @Test
    void 차량_대절_신청_폼_수정에_성공한다() {
        // given
        var memberId = 1L;
        var rent = fixtureMonkey.giveMeBuilder(Rent.class)
                .setNull("id")
                .set("memberId", memberId)
                .set("additionalInfo.recruitmentCount", 20)
                .sample();
        rentRepository.save(rent);
        var rentJoin = fixtureMonkey.giveMeBuilder(RentJoin.class)
                .setNull("id")
                .set("memberId", memberId)
                .set("rentId", rent.getId())
                .set("passengerNum", 5)
                .sample();
        rentJoinRepository.save(rentJoin);

        var rentJoinRequest = fixtureMonkey.giveMeBuilder(RentJoinUpdateRequest.class)
                .set("rentJoinId", rentJoin.getId())
                .set("passengerNum", 3)
                .sample();

        // when
        rentJoinCommandService.updateRentJoin(rentJoinRequest, memberId);

        // then
        assertSoftly(softly -> {
            softly.assertThat(rentJoin.getMemberId()).isEqualTo(memberId);
            softly.assertThat(rentJoin.getRentId()).isEqualTo(rent.getId());
            softly.assertThat(rentJoin.getPassengerNum()).isEqualTo(rentJoinRequest.passengerNum());
        });
    }

    @Test
    void 차량_대절_신청_폼이_없을_경우_예외를_발생시킨다() {
        // given
        var memberId = 1L;
        var rentJoinRequest = fixtureMonkey.giveMeOne(RentJoinUpdateRequest.class);

        // when & then
        assertThrows(RentJoinNotFoundException.class,
                () -> rentJoinCommandService.updateRentJoin(rentJoinRequest, memberId));
    }

    @Test
    void 차량_대절_신청_폼이_작성자_본인이_아니라면_예외를_발생시킨다() {
        // given
        var memberId = 1L;
        var rentJoin = fixtureMonkey.giveMeBuilder(RentJoin.class)
                .setNull("id")
                .set("memberId", memberId)
                .sample();
        rentJoinRepository.save(rentJoin);

        var anotherMemberId = 2L;
        var rentJoinRequest = fixtureMonkey.giveMeBuilder(RentJoinUpdateRequest.class)
                .set("rentJoinId", rentJoin.getId())
                .sample();

        // when & then
        assertThrows(RentJoinAccessDeniedException.class,
                () -> rentJoinCommandService.updateRentJoin(rentJoinRequest, anotherMemberId));
    }

    @Test
    void 차량_대절_신청_폼_삭제에_성공한다() {
        // given
        var memberId = 1L;
        var rent = fixtureMonkey.giveMeBuilder(Rent.class).setNull("id").sample();
        rentRepository.save(rent);

        var rentJoin = fixtureMonkey.giveMeBuilder(RentJoin.class)
                .setNull("id")
                .set("memberId", memberId)
                .set("rentId", rent.getId())
                .sample();
        rentJoinRepository.save(rentJoin);

        var rentJoinIdRequest = fixtureMonkey.giveMeBuilder(RentJoinIdRequest.class)
                .set("rentJoinId", rentJoin.getId())
                .sample();

        // when
        rentJoinCommandService.deleteRentJoin(rentJoinIdRequest, memberId);

        // then
        var deletedRentJoin = rentJoinRepository.findById(rentJoinIdRequest.rentJoinId()).orElse(null);
        assertThat(deletedRentJoin).isNull();
    }
}
