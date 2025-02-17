package com.backend.allreva.diary.command.application.request;

import com.backend.allreva.common.model.Image;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record UpdateDiaryRequest(

        @NotNull(message = "공연 기록을 선택해야합니다")
        Long diaryId,
        @NotNull(message = "공연을 선택해야합니다")
        Long concertId,
        @NotNull(message = "날짜를 선택해야합니다")
        LocalDate date,
        @NotNull(message = "회차를 선택해야합니다")
        String episode,

        String content,
        String seatName,
        List<Image> images
) {
}
