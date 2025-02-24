package com.backend.allreva.rent.query;

import com.backend.allreva.rent.infra.elasticsearch.RentDocument;
import com.backend.allreva.rent.infra.elasticsearch.RentDocumentRepository;
import com.backend.allreva.rent.query.application.RentSearchService;
import com.backend.allreva.rent.query.application.response.RentThumbnail;
import com.backend.allreva.support.IntegrationTestSupport;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RentSearchServiceTest extends IntegrationTestSupport {

    @Autowired
    RentSearchService rentSearchService;
    @Autowired
    RentDocumentRepository rentDocumentRepository;

    RentDocument rentDocument1;
    RentDocument rentDocument2;
    RentDocument rentDocument3;

    @BeforeAll
    void beforeAll() {
        rentDocument1 = RentDocument.builder()
                .id("1")
                .boardingArea("test1")
                .title("아기사자1")
                .imageUrl("https://cdn.dailysportshankook.co.kr/news/photo/202304/301834_302020_5359.jpg")
                .edDate(LocalDate.now())
                .build();

        rentDocument2 = RentDocument.builder()
                .id("2")
                .boardingArea("test2")
                .title("아기사자2")
                .imageUrl("https://cdn.dailysportshankook.co.kr/news/photo/202304/301834_302020_5359.jpg")
                .edDate(LocalDate.now())
                .build();

        rentDocument3 = RentDocument.builder()
                .id("3")
                .boardingArea("test3")
                .title("아기사자3")
                .imageUrl("https://cdn.dailysportshankook.co.kr/news/photo/202304/301834_302020_5359.jpg")
                .edDate(LocalDate.now())
                .build();

        rentDocumentRepository.save(rentDocument1);
        rentDocumentRepository.save(rentDocument2);
        rentDocumentRepository.save(rentDocument3);

    }

    @AfterAll
    void afterAll() {
        rentDocumentRepository.delete(rentDocument1);
        rentDocumentRepository.delete(rentDocument2);
        rentDocumentRepository.delete(rentDocument3);
    }

    @Test
    @DisplayName("차대절 검색시 연관도 상위 2개의 썸네일 나온다.")
    void rentThumbnailTest() {
        //given
        //when
        List<RentThumbnail> result = rentSearchService.searchRentThumbnails("아기사자");

        //then
        assertThat(result.size()).isEqualTo(2);
    }
}