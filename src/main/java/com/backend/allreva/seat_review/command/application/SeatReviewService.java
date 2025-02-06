package com.backend.allreva.seat_review.command.application;

import com.backend.allreva.hall.command.ConcertHallService;
import com.backend.allreva.hall.command.domain.ConcertHall;
import com.backend.allreva.hall.exception.ConcertHallNotFoundException;
import com.backend.allreva.hall.infra.elasticcsearch.ConcertHallSearchRepository;
import com.backend.allreva.hall.query.domain.ConcertHallDocument;
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
    private final ConcertHallService concertHallService;
    private final ConcertHallSearchRepository concertHallSearchRepository;

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
                    .concertTitle(request.concertTitle())
                    .build());

            ConcertHall concertHall = concertHallService.updateConcertHallStar(savedSeatReview.getHallId(), savedSeatReview.getStar(), 1);

            ConcertHallDocument concertHallDocument = concertHallSearchRepository.findById(concertHall.getId()).orElseThrow(
                    ConcertHallNotFoundException::new
            );
            concertHallDocument.updateStar(concertHall.getStar());
            concertHallSearchRepository.save(concertHallDocument);

            return savedSeatReview.getId();
        }catch (Exception e){
            throw new SeatReviewSaveFailedException();
        }
    }

    public SeatReview updateSeatReview(
            final ReviewUpdateRequest request,
            final Member member) {
        SeatReview seatReview = seatReviewRepository.findById(request.seatReviewId()).orElseThrow(SeatReviewNotFoundException::new);
        int starDelta = request.star() - seatReview.getStar();
        validateWriter(seatReview.getMemberId(), member.getId());
        seatReview.updateSeatReview(request);

        ConcertHall concertHall = concertHallService.updateConcertHallStar(seatReview.getHallId(), starDelta, 0);

        ConcertHallDocument concertHallDocument = concertHallSearchRepository.findById(concertHall.getId()).orElseThrow(
                ConcertHallNotFoundException::new
        );

        concertHallDocument.updateStar(concertHall.getStar());
        concertHallSearchRepository.save(concertHallDocument);

        return seatReviewRepository.save(seatReview);
    }

    public void deleteSeatReview(final Long id, final Member member) {
        SeatReview seatReview = seatReviewRepository.findById(id).orElseThrow(SeatReviewNotFoundException::new);
        validateWriter(seatReview.getMemberId(), member.getId());

        ConcertHall concertHall = concertHallService.updateConcertHallStar(seatReview.getHallId(), -seatReview.getStar(), -1);

        ConcertHallDocument concertHallDocument = concertHallSearchRepository.findById(concertHall.getId()).orElseThrow(
                ConcertHallNotFoundException::new
        );
        concertHallDocument.updateStar(concertHall.getStar());
        concertHallSearchRepository.save(concertHallDocument);

        seatReviewRepository.delete(seatReview);
    }

    private void validateWriter(final Long writerId, final Long memberId) {
        if (!writerId.equals(memberId)) {
            throw new NotWriterException();
        }
    }
}
