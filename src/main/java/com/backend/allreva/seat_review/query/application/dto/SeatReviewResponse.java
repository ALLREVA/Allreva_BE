package com.backend.allreva.seat_review.query.application.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class SeatReviewResponse{
    private final Long reviewId;
    private final String seat;
    private final String content;
    private final int star;
    private final Long memberId;
    private final String hallId;
    private final LocalDate viewDate;
    private final LocalDateTime createdAt;
    private final List<String> imageUrls;
    private final String profileImageUrl;
    private final String nickname;
    private final boolean isWriter;
    private final String concertTitle;

    @QueryProjection
    public SeatReviewResponse(
            Long reviewId,
            String seat,
            String content,
            int star,
            Long memberId,
            String hallId,
            LocalDate viewDate,
            LocalDateTime createdAt,
            List<String> imageUrls,
            String profileImageUrl,
            String nickname,
            boolean isWriter,
            String concertTitle
    ) {
        this.reviewId = reviewId;
        this.seat = seat;
        this.content = content;
        this.star = star;
        this.memberId = memberId;
        this.hallId = hallId;
        this.viewDate = viewDate;
        this.createdAt = createdAt;
        this.imageUrls = imageUrls;
        this.profileImageUrl = profileImageUrl;
        this.nickname = nickname;
        this.isWriter = isWriter;
        this.concertTitle = concertTitle;
    }
}