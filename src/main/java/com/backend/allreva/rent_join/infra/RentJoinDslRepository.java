package com.backend.allreva.rent_join.infra;

import static com.backend.allreva.rent.command.domain.QRent.rent;
import static com.backend.allreva.rent.command.domain.QRentBoardingInfo.rentBoardingInfo;
import static com.backend.allreva.rent_join.command.domain.QRentJoin.rentJoin;

import com.backend.allreva.rent_join.query.response.RentJoinResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RentJoinDslRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * [Participate] 자신이 참여한 차 대절 조회
     */
    public List<RentJoinResponse> findByMemberId(final Long memberId) {
        return queryFactory.select(Projections.constructor(RentJoinResponse.class,
                        rent.id,
                        rent.detailInfo.title,
                        rentBoardingInfo.date,
                        rent.operationInfo.boardingArea,
                        rent.createdAt,
                        rent.additionalInfo.endDate,
                        rentBoardingInfo.recruitmentCount,
                        rentBoardingInfo.passengerCount,
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
                .join(rentBoardingInfo).on(rent.id.eq(rentBoardingInfo.rent.id))
                .join(rentJoin).on(
                        rentBoardingInfo.rent.id.eq(rentJoin.rentId)
                            .and(rentBoardingInfo.date.eq(rentJoin.boardingDate)
                ))
                .where(rentJoin.memberId.eq(memberId))
                .fetch();
    }
}
