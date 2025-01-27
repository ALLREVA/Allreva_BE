package com.backend.allreva.hall.query.application;

import com.backend.allreva.concert.infra.rdb.ConcertJpaRepository;
import com.backend.allreva.hall.command.domain.ConcertHallRepository;
import com.backend.allreva.hall.exception.ConcertHallNotFoundException;
import com.backend.allreva.hall.exception.RelatedConcertException;
import com.backend.allreva.hall.infra.elasticcsearch.ConcertHallSearchRepository;
import com.backend.allreva.hall.query.application.response.ConcertHallDetailResponse;
import com.backend.allreva.hall.query.application.response.ConcertHallMainResponse;
import com.backend.allreva.hall.query.application.response.ConcertHallThumbnail;
import com.backend.allreva.hall.query.domain.ConcertHallDocument;
import com.backend.allreva.hall.query.application.response.RelatedConcertResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ConcertHallQueryService {

    private final ConcertHallRepository concertHallRepository;
    private final ConcertHallSearchRepository concertHallSearchRepository;
    private final ConcertJpaRepository concertJpaRepository;

    public ConcertHallDetailResponse findDetailByHallCode(final String hallCode) {
        return concertHallRepository.findDetailByHallCode(hallCode);
    }

    @Cacheable(
            cacheNames = "concertHallMain",
            key = "#address + '_' + #seatScale + '_' + #size + '_' + (#searchAfter != null ? #searchAfter.toString() : 'null')",
            unless = "#result == null",
            cacheManager = "concertHallMainCacheManager"
    )
    public ConcertHallMainResponse getConcertHallMain(
            final String address,
            final int seatScale,
            final List<Object> searchAfter,
            final int size
    ) {
        SearchHits<ConcertHallDocument> searchHits = concertHallSearchRepository.searchMainConcertHall(
                address,
                seatScale,
                searchAfter,
                size + 1
        );
        log.info("searchHits count : {}", searchHits.getTotalHits());
        List<ConcertHallThumbnail> concertHall = searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .map(ConcertHallThumbnail::from)
                .limit(size)
                .toList();

        if(concertHall.isEmpty()) {
            throw new ConcertHallNotFoundException();
        }

        boolean hasNext = searchHits.getSearchHits().size() > size;
        List<Object> nextSearchAfter = hasNext ?
                searchHits.getSearchHits().get(size -1).getSortValues() : null;
        return ConcertHallMainResponse.from(concertHall, nextSearchAfter);
    }

    @Cacheable(
            cacheNames = "relatedConcert",
            key = "#hallCode + '_' + #lastId + '_' + #pageSize",
            unless = "#result == null",
            cacheManager = "relatedConcertCacheManager"
    )
    public List<RelatedConcertResponse> getRelatedConcert(
            final String hallCode, final Long lastId, final Long lastViewCount, final int pageSize
    ) {
        try{
            return concertJpaRepository.findRelatedConcertsByHall(hallCode, lastId,lastViewCount, pageSize);
        }catch (Exception e){
            throw new RelatedConcertException();
        }

    }
}
