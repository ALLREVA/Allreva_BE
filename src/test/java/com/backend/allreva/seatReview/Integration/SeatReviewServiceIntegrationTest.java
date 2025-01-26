package com.backend.allreva.seatReview.Integration;

import com.backend.allreva.member.command.domain.Member;
import com.backend.allreva.member.command.domain.MemberRepository;
import com.backend.allreva.seat_review.command.application.SeatReviewService;
import com.backend.allreva.seat_review.command.application.dto.ReviewCreateRequest;
import com.backend.allreva.seat_review.command.domain.SeatReview;
import com.backend.allreva.seat_review.exception.SeatReviewSaveFailedException;
import com.backend.allreva.seat_review.infra.SeatReviewRepository;
import com.backend.allreva.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
public class SeatReviewServiceIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private SeatReviewService seatReviewService;
    @Autowired
    private SeatReviewRepository seatReviewRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("좌석리뷰 정상 생성 테스트")
    void testCreateSeatReview() {
        //given
        ReviewCreateRequest request = new ReviewCreateRequest(
                LocalDate.now(),
                "test"
                , 5,
                "A",
                "test",
                "123"
        );


        Member member = memberRepository.save(createTestMember());

        //when
        Long seatReview = seatReviewService.createSeatReview(request, member);

        //then
        Optional<SeatReview> findSeatReview = seatReviewRepository.findById(seatReview);

        findSeatReview.ifPresent(review -> {
            assertThat(review.getSeat()).isEqualTo(request.seat());
            assertThat(review.getStar()).isEqualTo(request.star());
            assertThat(review.getSeat()).isEqualTo(request.seat());
            assertThat(review.getContent()).isEqualTo(request.content());
            assertThat(review.getHallId()).isEqualTo(request.hallId());
        });

    }

    @Test
    @DisplayName("좌석리뷰 예외 테스트")
    void testCreateSeatReviewFailure() {
        //given
        ReviewCreateRequest request = new ReviewCreateRequest(
                LocalDate.now(),
                "test"
                , 5,
                "A",
                "test",
                "123"
        );
        //when
        //then
        assertThrows(SeatReviewSaveFailedException.class, () -> seatReviewService.createSeatReview(request, null));
    }
}
