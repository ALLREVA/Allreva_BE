package com.backend.allreva.keyword.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
@RequiredArgsConstructor
public class PopularKeywordScheduler {

    private final PopularKeywordCommandService popularKeywordCommandService;
    private static final String ONE_HOUR_CRON = "0 0 * * * *"; //초 분 시 일 월 요일

    @Scheduled(cron = ONE_HOUR_CRON) // 매일 새벽 4시에 업데이트
    public void updatePopularKeywordRank() {
        try {
            LocalDateTime now = LocalDateTime.now();
            String formattedDate = now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss"));
            popularKeywordCommandService.decreaseAllKeywordCount();
            popularKeywordCommandService.updatePopularKeywordRank();
            log.info(" {} : popular keyword rank updatePreviewMessage complete", formattedDate);
        } catch (Exception e) {
            log.error("can't updatePreviewMessage popular keyword rank . Message: {}", e.getMessage());
        }
    }
}
