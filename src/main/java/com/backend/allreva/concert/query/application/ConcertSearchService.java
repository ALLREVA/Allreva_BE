package com.backend.allreva.concert.query.application;


import com.backend.allreva.concert.infra.elasticsearch.ConcertSearchRepository;
import com.backend.allreva.concert.query.application.response.ConcertThumbnail;
import com.backend.allreva.concert.exception.search.ElasticSearchException;
import com.backend.allreva.concert.exception.search.SearchResultNotFoundException;
import com.backend.allreva.concert.query.application.response.ConcertSearchListResponse;
import com.backend.allreva.concert.infra.elasticsearch.ConcertDocument;
import com.backend.allreva.keyword.command.PopularKeywordCommandService;
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
    private final PopularKeywordCommandService popularKeywordCommandService;

    public List<ConcertThumbnail> searchConcertThumbnails(final String title) {
        try {
            popularKeywordCommandService.updateKeywordCount(title);

            List<ConcertDocument> content = concertSearchRepository.findByTitleMixed(
                    title, PageRequest.of(0, 2)).getContent();
            if (content.isEmpty()) {
                throw new SearchResultNotFoundException();
            }

            return content.stream()
                    .map(ConcertThumbnail::from)
                    .toList();
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

    public ConcertSearchListResponse searchAllConcertList(
            final String title,
            final List<Object> searchAfter,
            final int size
    ) {
        SearchHits<ConcertDocument> searchHits = concertSearchRepository.searchByTitleListAll(title, searchAfter, size + 1);
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
