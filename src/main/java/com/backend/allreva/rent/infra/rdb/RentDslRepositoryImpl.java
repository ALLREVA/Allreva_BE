package com.backend.allreva.rent.infra.rdb;

import static com.backend.allreva.concert.command.domain.QConcert.concert;
import static com.backend.allreva.hall.command.domain.QConcertHall.concertHall;
import static com.backend.allreva.rent.command.domain.QRent.rent;
import static com.backend.allreva.rent.command.domain.QRentBoardingDate.rentBoardingDate;
import static com.backend.allreva.rent_join.command.domain.QRentJoin.rentJoin;

import com.backend.allreva.common.util.DateHolder;
import com.backend.allreva.rent.command.domain.value.Region;
import com.backend.allreva.rent.query.application.response.DepositAccountResponse;
import com.backend.allreva.rent.query.application.response.RentAdminSummaryResponse;
import com.backend.allreva.rent.query.application.response.RentDetailResponse;
import com.backend.allreva.rent.query.application.response.RentDetailResponse.RentBoardingDateResponse;
import com.backend.allreva.rent.query.application.response.RentJoinCountResponse;
import com.backend.allreva.rent.query.application.response.RentJoinDetailResponse;
import com.backend.allreva.rent.query.application.response.RentSummaryResponse;
import com.backend.allreva.rent_join.command.domain.value.BoardingType;
import com.backend.allreva.rent_join.command.domain.value.RefundType;
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
    private final DateHolder dateHolder;

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
                        rent.additionalInfo.endDate.goe(dateHolder.getDate()), // TODO: 마감 기한 스케쥴러 구현 시 조건 1개 빼도 될듯
                        rent.isClosed.eq(false),
                        getRegionCondition(region),
                        getPagingCondition(sortType, lastId, lastEndDate)
                )
                .groupBy(rent.id)
                .orderBy(orderSpecifiers(sortType))
                .limit(pageSize)
                .fetch();
    }

    public List<RentSummaryResponse> findRentMainSummaries() {
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
                        rent.additionalInfo.endDate.goe(dateHolder.getDate()),
                        rent.isClosed.eq(false)
                )
                .groupBy(rent.id)
                .orderBy(rent.additionalInfo.endDate.asc())
                .limit(3)
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
    public Optional<RentDetailResponse> findRentDetailById(final Long rentId) {
        Optional<RentDetailResponse> rentDetailResponse = Optional.ofNullable(queryFactory
                .select(rentDetailProjections())
                .from(rent)
                .where(rent.id.eq(rentId))
                .leftJoin(concert).on(rent.concertId.eq(concert.id))
                .leftJoin(concertHall).on(concert.code.hallCode.eq(concertHall.id))
                .fetchFirst());

        List<RentBoardingDateResponse> rentBoardingDateResponses = queryFactory
                .select(rentBoardingDateProjection())
                .from(rent)
                .join(rentBoardingDate).on(rent.id.eq(rentBoardingDate.rent.id))
                .leftJoin(rentJoin).on(rentBoardingDate.date.eq(rentJoin.boardingDate))
                .where(rent.id.eq(rentId))
                .groupBy(rentBoardingDate.date)
                .fetch();
        rentDetailResponse.ifPresent(detailResponse -> detailResponse.setBoardingDates(rentBoardingDateResponses));

        return rentDetailResponse;
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
                rent.operationInfo.bus.busSize,
                rent.operationInfo.bus.busType,
                rent.operationInfo.bus.maxPassenger,
                rent.operationInfo.price.roundPrice,
                rent.operationInfo.price.upTimePrice,
                rent.operationInfo.price.downTimePrice,
                rent.additionalInfo.recruitmentCount,
                rent.additionalInfo.endDate,
                rent.additionalInfo.chatUrl,
                rent.additionalInfo.refundType,
                rent.additionalInfo.information,
                rent.isClosed
        );
    }

    private ConstructorExpression<RentBoardingDateResponse> rentBoardingDateProjection() {
        return Projections.constructor(RentBoardingDateResponse.class,
                rentBoardingDate.date,
                rentJoin.passengerNum.sum().coalesce(0).intValue()
        );
    }

    /**
     * 입금 계좌 조회
     */
    public Optional<DepositAccountResponse> findDepositAccountById(final Long rentId) {
        DepositAccountResponse depositAccountResponse = queryFactory
                .select(Projections.constructor(DepositAccountResponse.class,
                        rent.detailInfo.depositAccount))
                .from(rent)
                .where(rent.id.eq(rentId))
                .fetchFirst();
        return Optional.ofNullable(depositAccountResponse);
    }

    /**
     * [Register] 등록한 차 대절 관리 리스트 조회
     *
     * @param memberId 등록자 ID
     * @return 차 대절 관리 리스트 조회 결과
     */
    public List<RentAdminSummaryResponse> findRentAdminSummaries(final Long memberId) {
        return queryFactory.select(Projections.constructor(RentAdminSummaryResponse.class,
                        rent.id,
                        rent.detailInfo.title,
                        rentBoardingDate.date,
                        rent.operationInfo.boardingArea,
                        rent.createdAt,
                        rent.additionalInfo.endDate,
                        rent.additionalInfo.recruitmentCount,
                        rentJoin.passengerNum.sum().intValue(),
                        rent.isClosed,
                        rent.operationInfo.bus.busSize,
                        rent.operationInfo.bus.busType,
                        rent.operationInfo.bus.maxPassenger
                ))
                .from(rent)
                .leftJoin(rentBoardingDate).on(rent.id.eq(rentBoardingDate.rent.id))
                .leftJoin(rentJoin).on(rentBoardingDate.date.eq(rentJoin.boardingDate)
                        .and(rentBoardingDate.rent.id.eq(rentJoin.rentId)))
                .where(rent.memberId.eq(memberId))
                .groupBy(rent.id, rentBoardingDate.date)
                .fetch();
    }

    /**
     * [Register] 자신이 등록한 차 대절 관리 상세 조회
     *
     * @param memberId     등록자 ID
     * @param boardingDate 차 대절 날짜
     * @param rentId       차 대절 ID
     * @return 차 대절 관리 상세 조회 결과
     */
    public Optional<RentAdminSummaryResponse> findRentAdminSummary(
            final Long rentId,
            final LocalDate boardingDate,
            final Long memberId
    ) {
        RentAdminSummaryResponse rentAdminSummaryResponse = queryFactory.select(
                        Projections.constructor(RentAdminSummaryResponse.class,
                                rent.id,
                                rent.detailInfo.title,
                                rentBoardingDate.date,
                                rent.operationInfo.boardingArea,
                                rent.createdAt,
                                rent.additionalInfo.endDate,
                                rent.additionalInfo.recruitmentCount,
                                rentJoin.passengerNum.sum().intValue(),
                                rent.isClosed,
                                rent.operationInfo.bus.busSize,
                                rent.operationInfo.bus.busType,
                                rent.operationInfo.bus.maxPassenger
                        ))
                .from(rent)
                .leftJoin(rentBoardingDate).on(rent.id.eq(rentBoardingDate.rent.id))
                .leftJoin(rentJoin)
                .on(rentJoin.rentId.eq(rent.id)
                        .and(rentBoardingDate.date.eq(rentJoin.boardingDate))
                )
                .where(
                        rent.id.eq(rentId),
                        rentBoardingDate.date.eq(boardingDate),
                        rent.memberId.eq(memberId)
                )
                .groupBy(rent.id, rentBoardingDate.date)
                .fetchFirst();
        return Optional.ofNullable(rentAdminSummaryResponse);
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

    /**
     * [Register] 자신이 등록한 차 대절 참가자 리스트 조회
     *
     * @param memberId     등록자 ID
     * @param rentId       차 대절 ID
     * @param boardingDate 차 대절 날짜
     * @return 자신이 등록한 차 대절 참가자 리스트 조회 결과
     */
    public List<RentJoinDetailResponse> findRentJoinDetails(
            final Long memberId,
            final Long rentId,
            final LocalDate boardingDate
    ) {
        return queryFactory.select(Projections.constructor(RentJoinDetailResponse.class,
                        rentJoin.id,
                        rentJoin.createdAt,
                        rentJoin.depositor.depositorName,
                        rentJoin.depositor.phone,
                        rentJoin.passengerNum,
                        rentJoin.boardingType,
                        rentJoin.depositor.depositorTime,
                        rentJoin.refundType,
                        rentJoin.refundAccount
                ))
                .from(rentJoin)
                .join(rent).on(rentJoin.rentId.eq(rent.id))
                .where(
                        rentJoin.rentId.eq(rentId),
                        rentJoin.boardingDate.eq(boardingDate),
                        rent.memberId.eq(memberId)
                )
                .fetch();
    }
}
