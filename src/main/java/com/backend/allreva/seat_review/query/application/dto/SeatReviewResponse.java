package com.backend.allreva.seat_review.query.application.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class SeatReviewResponse{
    private Long reviewId;
    private String seat;
    private String content;
    private int star;
    private Long memberId;
    private String hallId;
    private LocalDate viewDate;
    private LocalDateTime createdAt;
    private List<String> imageUrls;
    private String profileImageUrl;
    private String nickname;
    private boolean isWriter;

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
            boolean isWriter
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
    }
}