package com.backend.allreva.hall.query.application.response;

import java.util.List;

public record ConcertHallMainResponse(
        List<ConcertHallThumbnail> concertHallThumbnails,
        List<Object> searchAfter
) {
    public static ConcertHallMainResponse from(final List<ConcertHallThumbnail> thumbnails, final List<Object> searchAfter) {
        return new ConcertHallMainResponse(thumbnails, searchAfter);
    }
}
