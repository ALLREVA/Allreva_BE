package com.backend.allreva.concert.infra.elasticsearch;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConcertSearchRepository extends
        ElasticsearchRepository<ConcertDocument, String>,
        CustomConcertSearchRepo {
    @Query("""
    {
      "bool": {
        "must": [
          {
            "match": {
              "title.mixed": {
                "query": "?0",
                "fuzziness": "AUTO"
              }
            }
          }
        ]
      }
    }
    """)
    Page<ConcertDocument> findByTitleMixed(String title, Pageable pageable);

    Optional<ConcertDocument> findByConcertCode(String concertCode);
}
