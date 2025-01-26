package com.backend.allreva.hall.query.application.response;

import java.time.LocalDate;

public record RelatedConcertResponse(
        Long id,
        String title,
        LocalDate startDate,
        LocalDate endDate,
        String imageUrl
) {
}
