package com.backend.allreva.keyword.command;

import com.backend.allreva.keyword.infra.PopularKeywordRepository;
import com.backend.allreva.keyword.query.application.dto.ChangeStatus;
import com.backend.allreva.keyword.query.application.dto.PopularKeywordResponse;
import com.backend.allreva.keyword.query.application.dto.PopularKeywordResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PopularKeywordCommandService {
    private final PopularKeywordRepository popularKeywordRepository;
    public static final Integer NOT_EXIST_RANK = -1;

    /**
     * 검색어 count updatePreviewMessage
     */
    public void updateKeywordCount(final String keyword) {
        popularKeywordRepository.updateKeywordCount(keyword, 1.0);
    }

    /**
     * 가중치 낮춤
     */
    public void decreaseAllKeywordCount() {
        popularKeywordRepository.decreaseAllKeywordCount();
    }

    /**
     * 인기검색어 스케줄러를 통해 1시간마다 순위 업데이트
     */
    public void updatePopularKeywordRank() {
        //1. 인기 검색어 목록 가져오기
        PopularKeywordResponses responses = popularKeywordRepository.getPopularKeywordRank();

        List<String> top10 = Optional.ofNullable(responses)
                .map(PopularKeywordResponses::popularKeywordResponses)
                .orElse(Collections.emptyList())
                .stream()
                .map(PopularKeywordResponse::keyword)
                .toList();



        //2. 현재 랭킹가져오기
        List<String> updatedTop10 = popularKeywordRepository.getTop10Keywords();

        //3. 랭킹 업데이트
        compareAndSaveRankChanges(top10, updatedTop10);
    }

    public void compareAndSaveRankChanges(final List<String> top10, final List<String> updatedTop10) {
        List<PopularKeywordResponse> list = new ArrayList<>();
        for (int i = 0; i < updatedTop10.size(); i++) {
            String keyword = updatedTop10.get(i);
            int rank = top10.indexOf(keyword);

            // 순위 비교
            ChangeStatus status = getChangeStatus(rank, i);

            list.add(PopularKeywordResponse.builder()
                    .rank(i + 1)
                    .keyword(keyword)
                    .changeStatus(status)
                    .build());
        }

        popularKeywordRepository.updatePopularKeywordRank(new PopularKeywordResponses(list));
    }

    private ChangeStatus getChangeStatus(int oldRank, int rank) {
        if (oldRank == NOT_EXIST_RANK) {
            return ChangeStatus.UP;
        }
        if (rank < oldRank) {
            return ChangeStatus.UP;
        }
        if (rank > oldRank) {
            return ChangeStatus.DOWN;
        }
        return ChangeStatus.STAY;
    }
}
