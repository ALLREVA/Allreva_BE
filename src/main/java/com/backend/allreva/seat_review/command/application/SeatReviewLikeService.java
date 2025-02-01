package com.backend.allreva.seat_review.command.application;

import com.backend.allreva.member.command.domain.Member;
import com.backend.allreva.seat_review.command.application.dto.SeatReviewLikeRequest;
import com.backend.allreva.seat_review.command.domain.SeatReviewLike;
import com.backend.allreva.seat_review.exception.DuplicateLikeException;
import com.backend.allreva.seat_review.exception.NotLikeMemberException;
import com.backend.allreva.seat_review.infra.SeatReviewLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SeatReviewLikeService {
    private final SeatReviewLikeRepository seatReviewLikeRepository;

    public Long increaseSeatReviewLike(
            final SeatReviewLikeRequest request,
            final Member member
    ) {
        if (checkMemberLike(request.seatReviewId(), member.getId())) {
            throw new DuplicateLikeException();
        }

        SeatReviewLike seatReviewLike = seatReviewLikeRepository.save(
                SeatReviewLike.builder()
                        .reviewId(request.seatReviewId())
                        .memberId(member.getId())
                        .build()
        );

        return seatReviewLike.getId();
    }

    public void cancelSeatReviewLike(
            final Long seatReviewId,
            final Member member
    ) {
        System.out.println(member.getId());
        if (checkMemberLike(seatReviewId, member.getId())) {
            throw new NotLikeMemberException();
        }

        seatReviewLikeRepository.deleteById(seatReviewId);
    }

    private boolean checkMemberLike(
            final Long seatReviewId,
            final Long memberId
    ){
        return seatReviewLikeRepository.existsByReviewIdAndMemberId(seatReviewId, memberId);
    }
}
