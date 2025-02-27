package com.backend.allreva.seat_review.command.application.dto;

import com.backend.allreva.common.model.Image;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record ReviewUpdateRequest(

        @NotNull(message = "seatReviewId는 필수입니다")
        Long seatReviewId,

        @NotNull(message = "관란일자는 필수 입니다.")
        LocalDate viewDate,

        @NotBlank(message = "공연 제목은 필수입니다.")
        String ConcertTitle,

        @NotNull(message = "별점은 필수입니다.")
        @Min(value = 0, message = "별점은 최소 1점 이상이어야 합니다.")
        @Max(value = 5, message = "별점은 최대 5점까지 가능합니다.")
        int star,

        @NotBlank(message = "좌석 정보는 필수입니다.")
        String seat,

        @NotBlank(message = "내용 입력은 필수입니다.")
        String content,

        @NotNull(message = "hallId는 필수입니다.")
        String hallId,

        List<Image> imageUrls,

        @NotNull(message = "concertTitle은 필수입니다.")
        String concertTitle
) {
}
