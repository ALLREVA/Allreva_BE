package com.backend.allreva.rent.application;

import static com.backend.allreva.support.FixtureUtil.fixtureMonkey;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

import com.backend.allreva.common.application.S3ImageService;
import com.backend.allreva.rent.command.application.RentCommandService;
import com.backend.allreva.rent.command.application.request.RentIdRequest;
import com.backend.allreva.rent.command.application.request.RentRegisterRequest;
import com.backend.allreva.rent.command.application.request.RentUpdateRequest;
import com.backend.allreva.rent.command.domain.Rent;
import com.backend.allreva.rent.command.domain.RentRepository;
import com.backend.allreva.rent.exception.RentAccessDeniedException;
import com.backend.allreva.rent.exception.RentNotFoundException;
import com.backend.allreva.rent.fake.RentFakeRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@Slf4j
@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
class RentCommandServiceTest {

    private final RentCommandService rentCommandService;
    private final RentRepository rentRepository;
    private final S3ImageService s3ImageService;

    public RentCommandServiceTest() {
        this.rentRepository = new RentFakeRepository();
        this.s3ImageService = Mockito.mock(S3ImageService.class);
        this.rentCommandService = new RentCommandService(rentRepository, s3ImageService);
    }

    @Test
    void 차량_대절_폼_개설을_성공한다() {
        // given
        var memberId = 1L;
        var rentRegisterRequest = fixtureMonkey.giveMeBuilder(RentRegisterRequest.class)
                .setNull("id")
                .sample();

        // when
        var registeredRentId = rentCommandService.registerRent(rentRegisterRequest, memberId);

        // then
        rentRepository.findById(registeredRentId).ifPresent(rent -> {
            assertSoftly(softly -> {
                softly.assertThat(rent.getMemberId()).isEqualTo(memberId);
                softly.assertThat(rent.getDetailInfo().getTitle()).isEqualTo(rentRegisterRequest.title());
            });
        });
    }

    @Test
    void 차량_대절_폼_수정을_성공한다() {
        // given
        var memberId = 1L;
        var rent = fixtureMonkey.giveMeBuilder(Rent.class)
                .setNull("id")
                .set("memberId", memberId)
                .sample();
        rentRepository.save(rent);

        var rentRequest = fixtureMonkey.giveMeBuilder(RentUpdateRequest.class)
                .set("rentId", rent.getId())
                .sample();

        // when
        Rent updatedRent = rentCommandService.updateRent(rentRequest, memberId);

        // then
        assertSoftly(softly -> {
            softly.assertThat(updatedRent.getId()).isEqualTo(rentRequest.rentId());
            softly.assertThat(updatedRent.getDetailInfo().getImage()).isEqualTo(rentRequest.image());
            softly.assertThat(updatedRent.getOperationInfo().getBus().getBusSize()).isEqualTo(rentRequest.busSize());
        });
    }

    @Test
    void 차량_대절_폼_수정_시_차량_대절_폼이_없을_경우_예외를_발생시킨다() {
        // given
        var memberId = 1L;
        var rentUpdateRequest = fixtureMonkey.giveMeBuilder(RentUpdateRequest.class)
                .set("rentId", 1L)
                .sample();

        // when & then
        assertThrows(RentNotFoundException.class,
                () -> rentCommandService.updateRent(rentUpdateRequest, memberId));
    }

    @Test
    void 차량_대절_폼_수정_시_차량_대절_폼이_작성자_본인이_아니라면_예외를_발생시킨다() {
        // given
        var memberId = 1L;
        var rent = fixtureMonkey.giveMeBuilder(Rent.class)
                .setNull("id")
                .set("memberId", memberId)
                .sample();
        rentRepository.save(rent);

        var anotherMemberId = 2L;
        var rentUpdateRequest = fixtureMonkey.giveMeBuilder(RentUpdateRequest.class)
                .set("rentId", 1L)
                .sample();

        // when & then
        assertThrows(RentAccessDeniedException.class,
                () -> rentCommandService.updateRent(rentUpdateRequest, anotherMemberId));
    }

    @Test
    void 차량_대절_폼_마감을_성공한다() {
        // given
        var memberId = 1L;
        var rent = fixtureMonkey.giveMeBuilder(Rent.class)
                .setNull("id")
                .set("memberId", memberId)
                .sample();
        rentRepository.save(rent);

        var rentIdRequest = fixtureMonkey.giveMeBuilder(RentIdRequest.class)
                .set("rentId", rent.getId())
                .sample();

        // when
        rentCommandService.closeRent(rentIdRequest, memberId);

        // then
        rentRepository.findById(rentIdRequest.rentId()).ifPresent(r -> {
            assertSoftly(softly -> {
                softly.assertThat(r.getId()).isEqualTo(rentIdRequest.rentId());
                softly.assertThat(r.isClosed()).isTrue();
            });
        });
    }

    @Test
    void 차량_대절_폼_마감_시_차량_대절_폼이_없을_경우_예외를_발생시킨다() {
        // given
        var memberId = 1L;
        var rentIdRequest = fixtureMonkey.giveMeBuilder(RentIdRequest.class)
                .set("rentId", 1L)
                .sample();

        // when & then
        assertThrows(RentNotFoundException.class,
                () -> rentCommandService.closeRent(rentIdRequest, memberId));
    }

    @Test
    void 차량_대절_폼_마감_시_차량_대절_폼이_작성자_본인이_아니라면_예외를_발생시킨다() {
        // given
        var memberId = 1L;
        var rent = fixtureMonkey.giveMeBuilder(Rent.class)
                .setNull("id")
                .set("memberId", memberId)
                .sample();
        rentRepository.save(rent);

        var anotherMemberId = 2L;
        var rentIdRequest = fixtureMonkey.giveMeBuilder(RentIdRequest.class)
                .set("rentId", rent.getId())
                .sample();

        // when & then
        assertThrows(RentAccessDeniedException.class,
                () -> rentCommandService.closeRent(rentIdRequest, anotherMemberId));
    }

    @Test
    void 차량_대절_폼_삭제를_성공한다() {
        // given
        var memberId = 1L;
        var rent = fixtureMonkey.giveMeBuilder(Rent.class)
               .setNull("id")
               .set("memberId", memberId)
               .sample();
        rentRepository.save(rent);

        var rentIdRequest = fixtureMonkey.giveMeBuilder(RentIdRequest.class)
                .set("rentId", rent.getId())
                .sample();

        // when
        rentCommandService.deleteRent(rentIdRequest, memberId);

        // then
        verify(s3ImageService).delete(rent.getDetailInfo().getImage().getUrl());
        var deletedRent = rentRepository.findById(rentIdRequest.rentId()).orElse(null);
        assertThat(deletedRent).isNull();
    }

    @Test
    void 차량_대절_폼_삭제_시_차량_대절_폼이_없을_경우_예외를_발생시킨다() {
        // given
        var memberId = 1L;
        var rentIdRequest = fixtureMonkey.giveMeBuilder(RentIdRequest.class)
               .set("rentId", 1L)
              .sample();

        // when & then
        assertThrows(RentNotFoundException.class,
                () -> rentCommandService.deleteRent(rentIdRequest, memberId));
    }

    @Test
    void 차량_대절_폼_삭제_시_차량_대절_폼이_작성자_본인이_아니라면_예외를_발생시킨다() {
        // given
        var memberId = 1L;
        var rent = fixtureMonkey.giveMeBuilder(Rent.class)
              .setNull("id")
              .set("memberId", memberId)
              .sample();
        rentRepository.save(rent);

        var anotherMemberId = 2L;
        var rentIdRequest = fixtureMonkey.giveMeBuilder(RentIdRequest.class)
             .set("rentId", rent.getId())
             .sample();

        // when & then
        assertThrows(RentAccessDeniedException.class,
                () -> rentCommandService.deleteRent(rentIdRequest, anotherMemberId));
    }
}
