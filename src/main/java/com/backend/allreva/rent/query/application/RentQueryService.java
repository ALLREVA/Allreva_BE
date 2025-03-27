package com.backend.allreva.rent.query.application;

import com.backend.allreva.member.command.domain.Member;
import com.backend.allreva.rent.command.domain.Rent;
import com.backend.allreva.rent.command.domain.RentRepository;
import com.backend.allreva.rent.command.domain.value.Region;
import com.backend.allreva.rent.exception.RentNotFoundException;
import com.backend.allreva.rent.query.application.response.DepositAccountResponse;
import com.backend.allreva.rent.query.application.response.RentAdminDetailResponse;
import com.backend.allreva.rent.query.application.response.RentAdminSummaryResponse;
import com.backend.allreva.rent.query.application.response.RentDetailResponse;
import com.backend.allreva.rent.query.application.response.RentSummaryResponse;
import com.backend.allreva.rent_join.command.domain.RentJoinRepository;
import com.backend.allreva.rent_join.query.response.RentJoinCountResponse;
import com.backend.allreva.rent_join.query.response.RentJoinDetailResponse;
import com.backend.allreva.survey.query.application.response.SortType;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RentQueryService {
    
    private final RentRepository rentRepository;
    private final RentJoinRepository rentJoinRepository;

    public List<RentSummaryResponse> getRentMainSummaries() {
        return rentRepository.findRentSummaries(null, SortType.LATEST, null, null, 3);
    }

    public List<RentSummaryResponse> getRentSummaries(
            final Region region,
            final SortType sortType,
            final LocalDate lastEndDate,
            final Long lastId,
            final int pageSize
    ) {
        return rentRepository.findRentSummaries(region, sortType, lastEndDate, lastId, pageSize);
    }

    public List<RentAdminSummaryResponse> getRentAdminSummaries(
            final Long memberId,
            final Long lastId,
            final int pageSize
    ) {
        return rentRepository.findRentAdminSummaries(memberId, lastId, pageSize);
    }

    // TODO: data modeling 개편 필요
    public RentDetailResponse getRentDetail(final Long id, final Member member) {
        RentDetailResponse rentDetailResponse = rentRepository.findRentDetail(id)
                .orElseThrow(RentNotFoundException::new);
        // 만약 회원으로 접속했다면
        if (member != null) {
            // 날짜 별 차량 대절 신청 여부 확인
            rentDetailResponse.getBoardingDates().forEach(response ->
                    response.setIsApplied(
                            rentJoinRepository.exists(member.getId(), id, response.getDate())
                    )
            );
            // 환불 계좌 정보 확인
            rentDetailResponse.setRefundAccount(member.getRefundAccount());
        }
        return rentDetailResponse;
    }

    public DepositAccountResponse getDepositAccount(final Long id) {
        Rent rent = rentRepository.findById(id)
                .orElseThrow(RentNotFoundException::new);
        return DepositAccountResponse.from(rent);
    }

    public RentAdminDetailResponse getRentAdminDetail(
            final Long memberId,
            final LocalDate boardingDate,
            final Long rentId
    ) {
        return new RentAdminDetailResponse(
                getRentAdminSummary(memberId, boardingDate, rentId),
                rentRepository.findRentJoinCount(memberId, boardingDate, rentId)
                        .orElse(RentJoinCountResponse.EMPTY),
                getRentJoinDetails(boardingDate, rentId)
        );
    }

    private RentAdminSummaryResponse getRentAdminSummary(
            final Long memberId,
            final LocalDate boardingDate,
            final Long rentId
    ) {
        return rentRepository.findByIdAndMemberId(rentId, memberId)
                .flatMap(rent -> rent.getBoardingInfos().stream()
                        .filter(info -> info.getDate().equals(boardingDate))
                        .map(info -> RentAdminSummaryResponse.from(rent, info))
                        .findFirst())
                .orElseThrow(RentNotFoundException::new);
    }

    private List<RentJoinDetailResponse> getRentJoinDetails(
            final LocalDate boardingDate,
            final Long rentId
    ) {
        return rentJoinRepository.findByRentIdAndBoardingDate(rentId, boardingDate).stream()
                .map(RentJoinDetailResponse::from)
                .toList();
    }
}
