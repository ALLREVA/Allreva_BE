package com.backend.allreva.keyword.integration;

import com.backend.allreva.concert.exception.search.SearchResultNotFoundException;
import com.backend.allreva.concert.infra.elasticsearch.SortDirection;
import com.backend.allreva.concert.query.application.ConcertQueryService;
import com.backend.allreva.concert.query.application.ConcertSearchService;
import com.backend.allreva.concert.query.application.response.ConcertMainResponse;
import com.backend.allreva.concert.query.application.response.ConcertSearchListResponse;
import com.backend.allreva.concert.query.application.response.ConcertThumbnail;
import com.backend.allreva.support.IntegrationTestSupport;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
@Slf4j
class ConcertSearchServiceTest extends IntegrationTestSupport {
    @Autowired
    ConcertSearchService concertSearchService;
    @Autowired
    ConcertQueryService concertQueryService;

    @Test
    @DisplayName("정상적인 검색")
    void successTest() {
        //give
        ConcertMainResponse concertMain = concertQueryService.getConcertMain("", null, 1, SortDirection.DATE);
        String query = concertMain.concertThumbnails().get(0).title();

        //when
        List<ConcertThumbnail> result = concertSearchService.searchConcertThumbnails(query);

        //then
        assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("검색 결과가 없는 경우 예외 발생")
    void NotFoundExceptionTest() {
        //then
        assertThrows(SearchResultNotFoundException.class, () -> {
            concertSearchService.searchConcertThumbnails("");
        });
    }

    @Test
    @DisplayName("콘서트 검색어에 맞춰서 관련도순 정렬 이후 마지막 searchAfter 보내면 다음 페이지에서 첫 페이지로")
    void ConcertSearchListTest(){
        // given
        List<Object> searchAfter = new ArrayList<>();
        ConcertMainResponse concertMain = concertQueryService.getConcertMain("", null, 2, SortDirection.DATE);
        String query = concertMain.concertThumbnails().get(0).title() + concertMain.concertThumbnails().get(1).title();

        // when
        ConcertSearchListResponse response1 = concertSearchService.searchConcertList(query, searchAfter, 1);
        log.info("response1: {}", response1);
        ConcertSearchListResponse response2 = concertSearchService.searchConcertList(query, response1.searchAfter(), 1);
        log.info("response2: {}", response2);
        ConcertSearchListResponse response3 = concertSearchService.searchConcertList(query, searchAfter, 2);
        log.info("response3: {}", response3);
        // then
        assertThat(response2.concertThumbnails().get(0))
                .usingRecursiveComparison()
                .isEqualTo(response3.concertThumbnails().get(1));

    }

    @Test
    @DisplayName("검색 결과가 없는 경우 예외 발생")
    void ConcertSearchListExceptionTest(){
        ///given
        List<Object> searchAfter = new ArrayList<>();
        String query ="|||";

        //when
        //then
        assertThrows(SearchResultNotFoundException.class, () -> {
            concertSearchService.searchConcertList(query, searchAfter, 2);
        });
    }

}