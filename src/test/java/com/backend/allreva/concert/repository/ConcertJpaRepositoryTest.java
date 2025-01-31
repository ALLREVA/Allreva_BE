package com.backend.allreva.concert.repository;


//public class ConcertJpaRepositoryTest extends IntegrationTestSupport {
//
//    @Autowired
//    ConcertJpaRepository concertJpaRepository;
//
//    @Test
//    @DisplayName("콘서트 홀 관련 콘서트 페이지네이션 테스트 - viewCount 내림차순")
//    void RelatedConcertTest() {
//        // 1. 첫 번째 요청 (페이지 크기를 동적으로 설정)
//        int firstPageSize = 4; // 유동적으로 변경 가능
//        List<RelatedConcertResponse> firstPage =
//                concertJpaRepository.findRelatedConcertsByHall("FC001247-01", null, firstPageSize);
//
//        // 첫 번째 페이지 검증
//        assertThat(firstPage).hasSizeLessThanOrEqualTo(firstPageSize); // 반환된 데이터가 요청한 크기 이하인지 확인
//
//        // 2. 두 번째 요청 (첫 번째 요청의 절반 크기로 설정)
//        int secondPageSize = firstPageSize / 2; // 유동적으로 설정 (여기서는 절반 크기)
//        List<RelatedConcertResponse> secondPagePart1 =
//                concertJpaRepository.findRelatedConcertsByHall("FC001247-01", null, secondPageSize);
//
//        // 두 번째 페이지 검증
//        assertThat(secondPagePart1).hasSizeLessThanOrEqualTo(secondPageSize);
//
//        // 3. 세 번째 요청 (두 번째 요청의 마지막 ID를 기준으로)
//        List<RelatedConcertResponse> secondPagePart2 =
//                concertJpaRepository.findRelatedConcertsByHall("FC001247-01", secondPagePart1.get(secondPagePart1.size() - 1).id(), secondPageSize);
//
//        // 세 번째 페이지 검증
//        assertThat(secondPagePart2).hasSizeLessThanOrEqualTo(secondPageSize);
//
//        // 4. 전체 데이터 비교 (첫 번째 요청과 두 번의 두 번째 요청 결과를 합침)
//        List<Long> allIdsFromSecondPages = new ArrayList<>();
//        allIdsFromSecondPages.addAll(secondPagePart1.stream().map(RelatedConcertResponse::id).toList());
//        allIdsFromSecondPages.addAll(secondPagePart2.stream().map(RelatedConcertResponse::id).toList());
//
//        List<Long> allIdsFromFirstPage = firstPage.stream().map(RelatedConcertResponse::id).toList();
//
//        // 전체 데이터 검증 (첫 번째 요청과 두 번의 두 번째 요청 결과가 동일한지 확인)
//        assertThat(allIdsFromFirstPage).containsExactlyElementsOf(allIdsFromSecondPages);
//
//        // 추가 검증: 날짜 범위 및 필드 값 확인
//        for (RelatedConcertResponse response : firstPage) {
//            assertThat(response.startDate()).isBeforeOrEqualTo(response.endDate()); // 시작일이 종료일보다 빠르거나 같아야 함
//            assertThat(response.startDate()).isNotNull();
//            assertThat(response.endDate()).isNotNull();
//            assertThat(response.imageUrl()).isNotBlank(); // 이미지 URL이 비어있지 않은지 확인
//        }
//    }
//}

