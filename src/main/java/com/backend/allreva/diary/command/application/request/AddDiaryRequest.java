package com.backend.allreva.diary.command.application.request;

import com.backend.allreva.common.model.Image;
import com.backend.allreva.diary.command.domain.ConcertDiary;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record AddDiaryRequest(

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

    public ConcertDiary to() {
        return ConcertDiary.builder()
                .concertId(concertId)
                .diaryDate(date)
                .episode(episode)
                .content(content)
                .seatName(seatName)
                .build();
    }
}
