package com.backend.allreva.concert.infra.rdb;


import com.backend.allreva.concert.query.application.response.ConcertDetailResponse;
import com.backend.allreva.concert.query.application.response.ConcertThumbnail;
import com.backend.allreva.hall.query.application.response.RelatedConcertResponse;

import java.util.List;

public interface ConcertDslRepository {
    ConcertDetailResponse findDetailById(Long concertId);

    List<ConcertThumbnail> getConcertMainThumbnails();

    List<RelatedConcertResponse> findRelatedConcertsByHall(String hallCode, Long lastId, int pageSize);
}
