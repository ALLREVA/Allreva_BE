package com.backend.allreva.seat_review.command.application;

import com.backend.allreva.member.command.domain.Member;
import com.backend.allreva.seat_review.command.application.dto.ReviewCreateRequest;
import com.backend.allreva.seat_review.command.application.dto.ReviewUpdateRequest;
import com.backend.allreva.seat_review.command.domain.SeatReview;
import com.backend.allreva.seat_review.exception.NotWriterException;
import com.backend.allreva.seat_review.exception.SeatReviewNotFoundException;
import com.backend.allreva.seat_review.exception.SeatReviewSaveFailedException;
import com.backend.allreva.seat_review.infra.SeatReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SeatReviewService {
    private final SeatReviewRepository seatReviewRepository;

    public Long createSeatReview(
            final ReviewCreateRequest request,
            final Member member) {
        try {
            SeatReview savedSeatReview = seatReviewRepository.save(SeatReview.builder()
                    .seat(request.seat())
                    .content(request.content())
                    .star(request.star())
                    .memberId(member.getId())
                    .hallId(request.hallId())
                    .viewDate(request.viewDate())
                    .build());
            return savedSeatReview.getId();
        }catch (Exception e){
            throw new SeatReviewSaveFailedException();
        }
    }

    public Long updateSeatReview(
            final ReviewUpdateRequest request,
            final Member member) {
        SeatReview seatReview = seatReviewRepository.findById(request.seatReviewId()).orElseThrow(SeatReviewNotFoundException::new);
        validateWriter(seatReview.getMemberId(), member.getId());
        seatReview.updateSeatReview(request);
        return seatReviewRepository.save(seatReview).getId();
    }

    public void deleteSeatReview(final Long id, final Member member) {
        SeatReview seatReview = seatReviewRepository.findById(id).orElseThrow(SeatReviewNotFoundException::new);
        validateWriter(seatReview.getMemberId(), member.getId());
        seatReviewRepository.delete(seatReview);
    }

    private void validateWriter(final Long writerId, final Long memberId) {
        if (!writerId.equals(memberId)) {
            throw new NotWriterException();
        }
    }
}
