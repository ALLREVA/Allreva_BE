package com.backend.allreva.keyword.integration;

import com.backend.allreva.keyword.command.PopularKeywordCommandService;
import com.backend.allreva.keyword.query.application.PopularKeywordQueryService;
import com.backend.allreva.keyword.query.application.dto.ChangeStatus;
import com.backend.allreva.keyword.query.application.dto.PopularKeywordResponse;
import com.backend.allreva.keyword.query.application.dto.PopularKeywordResponses;
import com.backend.allreva.support.IntegrationTestSupport;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.List;

public class PopularKeywordCommandTest extends IntegrationTestSupport {

    @Autowired
    private PopularKeywordCommandService keywordCommandService;

    @Autowired
    private PopularKeywordQueryService keywordQueryService;


    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String KEYWORD_KEY = "keyword";
    private static final String POPULAR_KEYWORD_KEY = "popular-keyword";

    private static ZSetOperations<String, Object> zSetOperations;
    private static ValueOperations<String, Object> valueOperations;

    @BeforeEach
    void beforeEach() {
        zSetOperations = redisTemplate.opsForZSet();
        valueOperations = redisTemplate.opsForValue();
    }

    @AfterEach
    void afterEach() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @Test
    @DisplayName("키워드 Count를 updatePreviewMessage 한다.")
    void updateKeywordCount() {
        //given
        String keyword = "하현상";

        //when
        keywordCommandService.updateKeywordCount(keyword);

        //then
        Assertions.assertEquals(1.0, zSetOperations.score(KEYWORD_KEY, keyword));
    }

    @Test
    @DisplayName("랭킹을 updatePreviewMessage 하고 목록을 조회한다.")
    void updatePopularKeywordRank() {

        List<PopularKeywordResponse> keywordRankList = List.of(
                PopularKeywordResponse.builder().rank(1).keyword("하현상").changeStatus(ChangeStatus.UP).build(),
                PopularKeywordResponse.builder().rank(2).keyword("데이식스").changeStatus(ChangeStatus.DOWN).build(),
                PopularKeywordResponse.builder().rank(3).keyword("아이유").changeStatus(ChangeStatus.STAY).build()
        );

        valueOperations.set(POPULAR_KEYWORD_KEY, new PopularKeywordResponses(keywordRankList));

        redisTemplate.opsForZSet().incrementScore(KEYWORD_KEY, "하현상", 25.0);
        redisTemplate.opsForZSet().incrementScore(KEYWORD_KEY, "데이식스", 15.0);
        redisTemplate.opsForZSet().incrementScore(KEYWORD_KEY, "아이유",30.0);
        redisTemplate.opsForZSet().incrementScore(KEYWORD_KEY, "아이브",40.0);

        // When
        keywordCommandService.updatePopularKeywordRank();
        // Then: Assert that the rank order of the keywords has been updated
        List<PopularKeywordResponse> updatedTop10 = keywordQueryService.getPopularKeywordRank();
        Assertions.assertNotNull(updatedTop10);
        Assertions.assertEquals("아이브", updatedTop10.get(0).keyword());
        Assertions.assertEquals(1, updatedTop10.get(0).rank());
        Assertions.assertEquals(ChangeStatus.UP, updatedTop10.get(0).changeStatus());
    }
}
