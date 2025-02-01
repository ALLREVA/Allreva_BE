package com.backend.allreva.seat_review.infra;

import com.backend.allreva.member.command.domain.QMember;
import com.backend.allreva.seat_review.command.domain.QSeatReview;
import com.backend.allreva.seat_review.command.domain.QSeatReviewImage;
import com.backend.allreva.seat_review.query.application.dto.SeatReviewResponse;
import com.backend.allreva.seat_review.ui.SeatReviewSearchCondition;
import com.backend.allreva.seat_review.ui.SortType;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.querydsl.core.types.Projections.list;

@Repository
@RequiredArgsConstructor
public class SeatReviewRepositoryCustomImpl implements SeatReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private static final QSeatReview review = QSeatReview.seatReview;
    private static final QSeatReviewImage reviewImage = QSeatReviewImage.seatReviewImage;
    private static final QMember member = QMember.member;

    @Override
    public List<SeatReviewResponse> findReviewsWithNoOffset(SeatReviewSearchCondition condition, Long currentMemberId) {
        return queryFactory
                .select(Projections.constructor(SeatReviewResponse.class,
                        review.id,
                        review.seat,
                        review.content,
                        review.star,
                        review.memberId,
                        review.hallId,
                        review.viewDate,
                        review.createdAt,
                        list(JPAExpressions
                                .select(reviewImage.url)
                                .from(reviewImage)
                                .where(reviewImage.seatReviewId.eq(review.id))
                                .orderBy(reviewImage.orderNum.asc())),
                        member.memberInfo.profileImageUrl,
                        member.memberInfo.nickname,
                        review.memberId.eq(condition.memberId())
                ))
                .from(review)
                .join(member).on(review.memberId.eq(member.id))
                .where(
                        createPaginationCondition(
                                condition.lastId(),
                                condition.lastCreatedAt(),
                                condition.sortType()
                        ),
                        review.hallId.eq(condition.hallId())
                )
                .orderBy(
                        getOrderSpecifier(condition.sortType()),
                        review.id.desc() // ID 보조 정렬 (항상 내림차순 고정)
                )
                .limit(condition.size())
                .fetch();
    }

    // 페이지네이션 조건 생성 (핵심 로직)
    private BooleanExpression createPaginationCondition(Long lastId, LocalDateTime lastCreatedAt, SortType sortType) {
        if (lastId == null || lastCreatedAt == null) return null;

        return sortType == SortType.CREATED_ASC
                ? review.createdAt.gt(lastCreatedAt) // 오래된 순: 이전 페이지보다 큰 시간
                .or(review.createdAt.eq(lastCreatedAt).and(review.id.lt(lastId)))
                : review.createdAt.lt(lastCreatedAt) // 최신순: 이전 페이지보다 작은 시간
                .or(review.createdAt.eq(lastCreatedAt).and(review.id.lt(lastId)));
    }

    private OrderSpecifier<?> getOrderSpecifier(SortType sortType) {
        return sortType == SortType.CREATED_ASC
                ? review.createdAt.asc()
                : review.createdAt.desc();
    }
}

