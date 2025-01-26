package com.backend.allreva.diary.query.response;

import com.backend.allreva.common.model.Image;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public record DiaryDetailResponse(

        String concertTitle,
        Image concertPoster,

        LocalDate diaryDate,
        String episode,
        String seatName,
        List<Image> diaryImages,
        String content
) {

}
