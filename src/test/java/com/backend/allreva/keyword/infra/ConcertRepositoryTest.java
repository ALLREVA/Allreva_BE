package com.backend.allreva.keyword.infra;

import com.backend.allreva.concert.infra.elasticsearch.ConcertSearchRepository;
import com.backend.allreva.concert.infra.elasticsearch.ConcertDocument;
import com.backend.allreva.support.IntegrationTestSupport;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@Slf4j
class ConcertRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private ConcertSearchRepository concertSearchRepository;


    @Test
    @DisplayName("검색어 에 따라 연관성 상위 2개가 출력")
    void findByTitleMixedTest() {
        //given
        PageRequest pageRequest = PageRequest.of(0, 2);

        //when
        List<ConcertDocument> day6 = concertSearchRepository.findByTitleMixed("day6", pageRequest).getContent();

        //then
        assertThat(day6.size(), is(2));
    }

    @Test
    @DisplayName("콘서트 코드로 검색")
    void findByConcertCodeTest() {
        //given
        String concertCode = "PF246277";

        //when
        ConcertDocument concertDocument = concertSearchRepository.findByConcertCode(concertCode).get();

        //then
        log.info("concertDocument: {}", concertDocument.toString());
        assertThat(concertDocument.getConcertCode(), is(concertCode));
    }

    @Test
    @DisplayName("조회수 올라감 테스트")
    void increaseViewCountTest() {
        //given
        String concertCode = "PF246277";
        ConcertDocument concertDocument = concertSearchRepository.findByConcertCode(concertCode).get();
        concertDocument.intiViewCount();
        concertSearchRepository.save(concertDocument);
        //when

        concertDocument.updateViewCount(10L);
        ConcertDocument save = concertSearchRepository.save(concertDocument);

        //then
        assertThat(concertDocument.getViewCount(), is(10L));

    }
}