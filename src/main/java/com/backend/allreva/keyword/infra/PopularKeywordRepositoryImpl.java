package com.backend.allreva.keyword.infra;

import com.backend.allreva.keyword.query.application.dto.PopularKeywordResponses;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class PopularKeywordRepositoryImpl implements PopularKeywordRepository {
    private static final String KEYWORD_KEY = "keyword";
    private static final String POPULAR_KEYWORD_KEY = "popular-keyword";

    @Resource(name = "redisTemplate")
    private ZSetOperations<String, String> zSetOperations;

    @Resource(name = "redisTemplate")
    private ValueOperations<String, Object> valueOperations;


    /**
     * 인기 검색어 top 10 조회
     */
    @Override
    public PopularKeywordResponses getPopularKeywordRank() {
        return (PopularKeywordResponses) valueOperations.get(POPULAR_KEYWORD_KEY);
    }

    @Override
    public void updatePopularKeywordRank(final PopularKeywordResponses list) {
        valueOperations.set(POPULAR_KEYWORD_KEY, list);
    }


    /**
     * 키워드 count updatePreviewMessage
     */
    @Override
    public void updateKeywordCount(final String keyword, final Double count) {
        zSetOperations.incrementScore(KEYWORD_KEY, keyword, count);
    }

    /**
     * 가중치 낮춤
     */
    @Override
    public void decreaseAllKeywordCount() {
        //전체 범위의 키워드 받아오기
        Set<ZSetOperations.TypedTuple<String>> keywords = zSetOperations.rangeWithScores(KEYWORD_KEY, 0, -1);

        if (keywords == null) return;

        keywords.forEach(keyword -> {
            if (keyword.getValue() == null || keyword.getScore() == null) return;

            // 50% 가중치 낮추기
            double newScore = keyword.getScore() * 0.5;
            zSetOperations.add(KEYWORD_KEY, keyword.getValue(), newScore);
        });
    }

    /**
     * 현재 top 10 조회
     */
    @Override
    public List<String> getTop10Keywords() {
        Set<String> result = zSetOperations.reverseRange(KEYWORD_KEY, 0, 9);
        if (result == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(result);
    }

}
