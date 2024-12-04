package com.backend.allreva.search.query.application;

import com.backend.allreva.concert.query.application.dto.ConcertThumbnail;
import com.backend.allreva.search.exception.ElasticSearchException;
import com.backend.allreva.search.exception.SearchResultNotFoundException;
import com.backend.allreva.search.infra.ConcertSearchRepository;
import com.backend.allreva.search.query.application.dto.ConcertSearchListResponse;
import com.backend.allreva.search.query.domain.ConcertDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConcertSearchService {
    private final ConcertSearchRepository concertSearchRepository;

    public List<ConcertDocument> searchConcertThumbnails(final String title) {
        try {
            List<ConcertDocument> content = concertSearchRepository.findByTitleMixed(
                    title, PageRequest.of(0, 2)).getContent();
            if (content.isEmpty()) {
                throw new SearchResultNotFoundException();
            }
            return content;
        }catch (ElasticSearchException e){
            throw new ElasticSearchException();
        }
    }

    public ConcertSearchListResponse searchConcertList(
            final String title,
            final List<Object> searchAfter,
            final int size
    ){
        SearchHits<ConcertDocument> searchHits = concertSearchRepository.searchByTitleList(title, searchAfter, size + 1);
        List<ConcertThumbnail> concertThumbnails = searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .map(ConcertThumbnail::from)
                .limit(size)
                .toList();

        if(concertThumbnails.isEmpty()) {
            throw new SearchResultNotFoundException();
        }

        boolean hasNext = searchHits.getSearchHits().size() > size;
        List<Object> nextSearchAfter = hasNext ?
                searchHits.getSearchHits().get(size -1).getSortValues() : null;
        return ConcertSearchListResponse.from(concertThumbnails, nextSearchAfter);
    }
}
