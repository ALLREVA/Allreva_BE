package com.backend.allreva.seat_review.infra;

import com.backend.allreva.seat_review.command.domain.QSeatReview;
import com.backend.allreva.seat_review.command.domain.QSeatReviewImage;
import com.backend.allreva.seat_review.command.domain.QSeatReviewLike;
import com.backend.allreva.seat_review.query.application.dto.SeatReviewResponse;
import com.backend.allreva.seat_review.ui.SeatReviewSearchCondition;
import com.backend.allreva.seat_review.ui.SortType;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.querydsl.core.types.Projections.list;

@Repository
@RequiredArgsConstructor
public class SeatReviewRepositoryCustomImpl implements SeatReviewRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    private static final QSeatReview review = QSeatReview.seatReview;
    private static final QSeatReviewImage reviewImage = QSeatReviewImage.seatReviewImage;
    private static final QSeatReviewLike reviewLike = QSeatReviewLike.seatReviewLike;

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
                        JPAExpressions
                                .select(reviewLike.count())
                                .from(reviewLike)
                                .where(reviewLike.reviewId.eq(review.id)),
                        JPAExpressions
                                .select(reviewLike.count().gt(0))
                                .from(reviewLike)
                                .where(
                                        reviewLike.reviewId.eq(review.id),
                                        reviewLike.memberId.eq(currentMemberId)
                                )
                ))
                .from(review)
                .where(
                        ltReviewId(condition.lastId()),
                        review.hallId.eq(condition.hallId())
                )
                .orderBy(getOrderSpecifier(condition.sortType()))
                .limit(condition.size())
                .fetch();
    }

    private BooleanExpression ltReviewId(Long lastId) {
        return lastId != null ? review.id.lt(lastId) : null;
    }

    private OrderSpecifier<?> getOrderSpecifier(SortType sortType) {
        if (sortType == SortType.LIKE_COUNT) {
            return new OrderSpecifier<>(Order.DESC,
                    JPAExpressions
                            .select(reviewLike.count())
                            .from(reviewLike)
                            .where(reviewLike.reviewId.eq(review.id))
            );
        }
        return review.createdAt.desc();
    }
}
