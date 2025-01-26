package com.backend.allreva.hall.infra.elasticcsearch;

import com.backend.allreva.hall.query.domain.ConcertHallDocument;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.util.List;

public interface CustomConcertHallSearchRepo {
    SearchHits<ConcertHallDocument> searchMainConcertHall(
            String address,
            Integer minSeatSize,
            List<Object> searchAfter,
            int size
    );
}
