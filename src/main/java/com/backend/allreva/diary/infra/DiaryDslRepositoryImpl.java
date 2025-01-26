package com.backend.allreva.diary.infra;

import com.backend.allreva.diary.query.response.DiaryDetailResponse;
import com.backend.allreva.diary.query.response.DiarySummaryResponse;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import static com.backend.allreva.common.model.QImage.image;
import static com.backend.allreva.concert.command.domain.QConcert.concert;
import static com.backend.allreva.diary.command.domain.QConcertDiary.concertDiary;

@RequiredArgsConstructor
@Repository
public class DiaryDslRepositoryImpl implements DiaryDslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public DiaryDetailResponse findDetail(
            final Long diaryId,
            final Long memberId
    ) {
        return queryFactory
                .from(concertDiary)
                .leftJoin(concertDiary.diaryImages, image)
                .join(concert).on(concert.id.eq(concertDiary.concertId))
                .where(concertDiary.id.eq(diaryId))
                .where(concertDiary.memberId.eq(memberId))
                .transform(GroupBy.groupBy(concertDiary.id)
                        .as(detailProjection()))
                .get(diaryId);
    }

    private ConstructorExpression<DiaryDetailResponse> detailProjection() {
        return Projections.constructor(DiaryDetailResponse.class,
                concert.concertInfo.title,
                concert.poster,
                concertDiary.diaryDate,
                concertDiary.episode,
                concertDiary.seatName,
                GroupBy.list(image),
                concertDiary.content
                );
    }

    @Override
    public List<DiarySummaryResponse> findSummaries(
            final Long memberId,
            final int year,
            final int month
    ) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        return queryFactory.select(summaryProjection())
                .from(concertDiary)
                .leftJoin(concert).on(concert.id.eq(concertDiary.concertId))
                .where(concertDiary.memberId.eq(memberId))
                .where(concertDiary.diaryDate.between(startDate, endDate))
                .fetch();
    }

    private ConstructorExpression<DiarySummaryResponse> summaryProjection() {
        return Projections.constructor(DiarySummaryResponse.class,
                concertDiary.id,
                concert.poster,
                concertDiary.diaryDate
        );
    }
}
