package com.backend.allreva.hall.infra.elasticcsearch;

import com.backend.allreva.hall.query.domain.ConcertHallDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ConcertHallSearchRepository extends
        ElasticsearchRepository<ConcertHallDocument, String>,
        CustomConcertHallSearchRepo {

}
