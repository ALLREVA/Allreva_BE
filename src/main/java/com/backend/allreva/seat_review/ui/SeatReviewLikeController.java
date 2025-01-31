package com.backend.allreva.seat_review.ui;

import com.backend.allreva.auth.security.AuthMember;
import com.backend.allreva.common.dto.Response;
import com.backend.allreva.member.command.domain.Member;
import com.backend.allreva.seat_review.command.application.SeatReviewLikeService;
import com.backend.allreva.seat_review.command.application.dto.SeatReviewLikeRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/seat-review-like")
public class SeatReviewLikeController implements SeatReviewLikeControllerSwagger {

    private final SeatReviewLikeService seatReviewLikeService;

    @Override
    @PostMapping
    public Response<Long> likeSeatReview(
                @RequestBody @Valid final SeatReviewLikeRequest request,
                @AuthMember final Member member
    ) {
        return Response.onSuccess(
                seatReviewLikeService.increaseSeatReviewLike(request, member)
        );
    }

    @Override
    @DeleteMapping
    public Response<Void> likeSeatReviewCancel(
            @RequestParam(required = true) final Long seatReviewLikeId,
            @AuthMember final Member member
    ) {
        seatReviewLikeService.cancelSeatReviewLike(seatReviewLikeId, member);
        return Response.onSuccess();
    }
}
