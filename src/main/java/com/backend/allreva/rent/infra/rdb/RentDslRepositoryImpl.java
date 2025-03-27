package com.backend.allreva.rent.infra.rdb;

import static com.backend.allreva.concert.command.domain.QConcert.concert;
import static com.backend.allreva.hall.command.domain.QConcertHall.concertHall;
import static com.backend.allreva.rent.command.domain.QRent.rent;
import static com.backend.allreva.rent.command.domain.QRentBoardingInfo.rentBoardingInfo;
import static com.backend.allreva.rent_join.command.domain.QRentJoin.rentJoin;

import com.backend.allreva.rent.command.domain.value.Region;
import com.backend.allreva.rent.query.application.response.RentAdminSummaryResponse;
import com.backend.allreva.rent.query.application.response.RentDetailResponse;
import com.backend.allreva.rent.query.application.response.RentDetailResponse.RentBoardingDateResponse;
import com.backend.allreva.rent.query.application.response.RentSummaryResponse;
import com.backend.allreva.rent_join.command.domain.value.BoardingType;
import com.backend.allreva.rent_join.command.domain.value.RefundType;
import com.backend.allreva.rent_join.query.response.RentJoinCountResponse;
import com.backend.allreva.survey.query.application.response.SortType;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RentDslRepositoryImpl {

    private final JPAQueryFactory queryFactory;

    /**
     * 차 대절 메인 페이지 조회
     */
    public List<RentSummaryResponse> findRentSummaries(
            final Region region,
            final SortType sortType,
            final LocalDate lastEndDate,
            final Long lastId,
            final int pageSize
    ) {
        return queryFactory
                .select(Projections.constructor(RentSummaryResponse.class,
                        rent.id,
                        rent.detailInfo.title,
                        rent.operationInfo.boardingArea,
                        rent.additionalInfo.endDate,
                        rent.detailInfo.image.url
                ))
                .from(rent)
                .where(
                        rent.isClosed.eq(false),
                        getRegionCondition(region),
                        getPagingCondition(sortType, lastId, lastEndDate)
                )
                .groupBy(rent.id)
                .orderBy(orderSpecifiers(sortType))
                .limit(pageSize)
                .fetch();
    }

    /**
     * [Admin] 차 대절 관리 페이지 조회
     */
    public List<RentAdminSummaryResponse> findRentAdminSummaries(
            final Long memberId,
            final Long lastId,
            final int pageSize
    ) {
        return queryFactory
                .select(Projections.constructor(RentAdminSummaryResponse.class,
                        rent.id,
                        rent.detailInfo.title,
                        rentBoardingInfo.date,
                        rent.operationInfo.boardingArea,
                        rent.createdAt,
                        rent.additionalInfo.endDate,
                        rentBoardingInfo.recruitmentCount,
                        rentBoardingInfo.passengerCount,
                        rent.isClosed,
                        rent.operationInfo.bus.busSize,
                        rent.operationInfo.bus.busType,
                        rent.operationInfo.bus.maxPassenger
                ))
                .from(rent)
                .join(rentBoardingInfo)
                    .on(rent.id.eq(rentBoardingInfo.rent.id))
                .where(
                        rent.memberId.eq(memberId),
                        getPagingCondition(SortType.LATEST, lastId, null)
                )
                .orderBy(orderSpecifiers(SortType.LATEST))
                .limit(pageSize)
                .fetch();
    }

    private BooleanExpression getRegionCondition(final Region region) {
        return region == null ? null : rent.detailInfo.region.eq(region);
    }

    private BooleanExpression getPagingCondition(
            final SortType sortType,
            final Long lastId,
            final LocalDate lastEndDate
    ) {
        if (lastId == null && lastEndDate == null) {
            return null;
        }

        switch (sortType) {
            case CLOSING -> {
                return (rent.additionalInfo.endDate.gt(lastEndDate))
                        .or(rent.additionalInfo.endDate.eq(lastEndDate).and(rent.id.gt(lastId)));
            }
            case OLDEST -> {
                return rent.id.gt(lastId);
            }
            default -> {
                return rent.id.lt(lastId);
            }
        }
    }

    private OrderSpecifier<?>[] orderSpecifiers(final SortType sortType) {
        switch (sortType) {
            case CLOSING -> {
                return new OrderSpecifier[]{
                        rent.additionalInfo.endDate.asc(),
                        rent.id.asc()
                };
            }
            case OLDEST -> {
                return new OrderSpecifier[]{
                        rent.id.asc()
                };
            }
            default -> {
                return new OrderSpecifier[]{
                        rent.id.desc()
                };
            }
        }
    }

    /**
     * 차 대절 상세 조회
     */
    public Optional<RentDetailResponse> findRentDetail(final Long rentId) {
        return Optional.ofNullable(queryFactory
                .select(rentDetailProjections())
                .from(rent)
                .join(rentBoardingInfo).on(rent.id.eq(rentBoardingInfo.rent.id))
                .leftJoin(concert).on(rent.concertId.eq(concert.id))
                .leftJoin(concertHall).on(concert.code.hallCode.eq(concertHall.id))
                .where(rent.id.eq(rentId))
                .fetchFirst());
    }

    private ConstructorExpression<RentDetailResponse> rentDetailProjections() {
        return Projections.constructor(RentDetailResponse.class,
                concert.concertInfo.title,
                rent.detailInfo.image.url,
                rent.detailInfo.title,
                rent.detailInfo.artistName,
                rent.detailInfo.region,
                rent.operationInfo.boardingArea, // 상행 지역
                concertHall.name, // 하행 지역
                rent.operationInfo.upTime,
                rent.operationInfo.downTime,
                Projections.list(
                        Projections.constructor(RentBoardingDateResponse.class,
                                rentBoardingInfo.date,
                                rentBoardingInfo.passengerCount
                        )
                ),
                rent.operationInfo.bus.busSize,
                rent.operationInfo.bus.busType,
                rent.operationInfo.bus.maxPassenger,
                rent.operationInfo.price.roundPrice,
                rent.operationInfo.price.upTimePrice,
                rent.operationInfo.price.downTimePrice,
                rentBoardingInfo.recruitmentCount,
                rent.additionalInfo.endDate,
                rent.additionalInfo.chatUrl,
                rent.additionalInfo.refundType,
                rent.additionalInfo.information,
                rent.isClosed
        );
    }

    /**
     * [Register] 자신이 등록한 차 대절 신청 인원 상세 조회
     *
     * @param memberId     등록자 ID
     * @param boardingDate 차 대절 날짜
     * @param rentId       차 대절 ID
     * @return 차 대절 신청 인원 상세 조회 결과
     */
    public Optional<RentJoinCountResponse> findRentJoinCount(
            final Long memberId,
            final LocalDate boardingDate,
            final Long rentId
    ) {
        RentJoinCountResponse rentJoinCountResponse = queryFactory.select(
                        Projections.constructor(RentJoinCountResponse.class,
                                getRentBoardingCount(BoardingType.UP, "RentUpCount"),
                                getRentBoardingCount(BoardingType.DOWN, "RentDownCount"),
                                getRentBoardingCount(BoardingType.ROUND, "RentRoundCount"),
                                getRefundCount(RefundType.REFUND, "refundCount"),
                                getRefundCount(RefundType.ADDITIONAL_DEPOSIT, "additionalDepositCount")))
                .from(rentJoin)
                .join(rent).on(rentJoin.rentId.eq(rent.id))
                .where(
                        rentJoin.rentId.eq(rentId),
                        rentJoin.boardingDate.eq(boardingDate),
                        rent.memberId.eq(memberId)
                )
                .groupBy(rentJoin.boardingType, rentJoin.refundType)
                .fetchFirst();
        return Optional.ofNullable(rentJoinCountResponse);
    }

    // BoardingType에 따른 case when절
    private NumberExpression<Integer> getRentBoardingCount(final BoardingType boardingType, final String alias) {
        return rentJoin.boardingType
                .when(boardingType)
                .then(rentJoin.passengerNum.sum().intValue())
                .otherwise(0)
                .as(alias);
    }

    // RefundType에 따른 case when절
    private NumberExpression<Integer> getRefundCount(final RefundType refundType, final String alias) {
        return rentJoin.refundType
                .when(refundType)
                .then(rentJoin.passengerNum.sum().intValue())
                .otherwise(0)
                .add(rentJoin.refundType
                        .when(RefundType.BOTH)
                        .then(rentJoin.passengerNum.sum().intValue())
                        .otherwise(0))
                .as(alias);
    }
}
