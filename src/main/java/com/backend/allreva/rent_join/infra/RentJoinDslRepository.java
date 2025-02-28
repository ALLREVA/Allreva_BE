package com.backend.allreva.rent_join.infra;

import static com.backend.allreva.rent.command.domain.QRent.rent;
import static com.backend.allreva.rent.command.domain.QRentBoardingDate.rentBoardingDate;
import static com.backend.allreva.rent_join.command.domain.QRentJoin.rentJoin;

import com.backend.allreva.rent.command.domain.QRent;
import com.backend.allreva.rent.command.domain.QRentBoardingDate;
import com.backend.allreva.rent_join.command.domain.QRentJoin;
import com.backend.allreva.rent_join.query.response.RentJoinResponse;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class RentJoinDslRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * [Count] 차 대절 현재 참여자 수 조회
     */
    public Integer countRentJoin(
            final Long rentId,
            final LocalDate boardingDate
    ) {
        return queryFactory.select(rentJoin.passengerNum.sum().coalesce(0))
                .from(rentJoin)
                .leftJoin(rentBoardingDate)
                    .on(rentBoardingDate.rent.id.eq(rentJoin.rentId))
                .where(
                        rentJoin.rentId.eq(rentId),
                        rentJoin.boardingDate.eq(boardingDate)
                )
                .fetchOne();
    }

    /**
     * [Participate] 자신이 참여한 차 대절 조회
     */
    public List<RentJoinResponse> findRentJoin(final Long memberId) {
        QRent rentSub = new QRent("rentSub");
        QRentBoardingDate rentBoardingDateSub = new QRentBoardingDate("rentBoardingDateSub");
        QRentJoin rentJoinSub = new QRentJoin("rentJoinSub");

        return queryFactory.select(Projections.constructor(RentJoinResponse.class,
                        rent.id,
                        rent.detailInfo.title,
                        rentBoardingDate.date,
                        rent.operationInfo.boardingArea,
                        rent.createdAt,
                        rent.additionalInfo.endDate,
                        rent.additionalInfo.recruitmentCount,
                        ExpressionUtils.as(
                                JPAExpressions.select(rentJoinSub.passengerNum.sum())
                                        .from(rentSub)
                                        .join(rentBoardingDateSub).on(rentSub.id.eq(rentBoardingDateSub.rent.id))
                                        .join(rentJoinSub).on(rentBoardingDateSub.date.eq(rentJoinSub.boardingDate))
                                        .where(rentSub.id.eq(rent.id)),
                                "passengerNum"
                        ),
                        rent.isClosed,
                        rentJoin.id,
                        rentJoin.createdAt,
                        rentJoin.passengerNum,
                        rentJoin.boardingType,
                        rentJoin.depositor.depositorName,
                        rentJoin.depositor.depositorTime,
                        rentJoin.refundType
                ))
                .from(rent)
                .join(rentBoardingDate).on(rent.id.eq(rentBoardingDate.rent.id))
                .join(rentJoin).on(rentBoardingDate.date.eq(rentJoin.boardingDate))
                .where(rentJoin.memberId.eq(memberId))
                .groupBy(rent.id, rentBoardingDate.date, rentJoin.id)
                .fetch();
    }
}
