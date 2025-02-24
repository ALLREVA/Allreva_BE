package com.backend.allreva.rent.query.application;

import com.backend.allreva.member.command.domain.Member;
import com.backend.allreva.rent.command.domain.RentRepository;
import com.backend.allreva.rent.command.domain.value.Region;
import com.backend.allreva.rent.exception.RentNotFoundException;
import com.backend.allreva.rent.query.application.response.*;
import com.backend.allreva.rent_join.command.domain.RentJoinRepository;
import com.backend.allreva.survey.query.application.response.SortType;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RentQueryService {

    private final RentRepository rentRepository;
    private final RentJoinRepository rentJoinRepository;

    public List<RentSummaryResponse> getRentSummaries(
            final Region region,
            final SortType sortType,
            final LocalDate lastEndDate,
            final Long lastId,
            final int pageSize
    ) {
        return rentRepository.findRentSummaries(region, sortType, lastEndDate, lastId, pageSize);
    }

    public List<RentSummaryResponse> getRentMainSummaries(){
        return rentRepository.findRentMainSummaries();
    }

    public RentDetailResponse getRentDetailById(final Long id, final Member member) {
        RentDetailResponse rentDetailResponse = rentRepository.findRentDetailById(id)
                .orElseThrow(RentNotFoundException::new);
        // 만약 회원으로 접속했다면
        if (member != null) {
            // 날짜 별 차량 대절 신청 여부 확인
            rentDetailResponse.getBoardingDates().forEach(response -> {
                        if (rentJoinRepository.existsByBoardingDateAndRentIdAndMemberId(response.getDate(), id, member.getId()))
                            response.setIsApplied(true);
                        else
                            response.setIsApplied(false);
            });
            // 환불 계좌 정보 확인
            rentDetailResponse.setRefundAccount(member.getRefundAccount());
        }
        return rentDetailResponse;
    }

    public DepositAccountResponse getDepositAccountById(final Long id) {
        return rentRepository.findDepositAccountById(id)
                .orElseThrow(RentNotFoundException::new);
    }

    public List<RentAdminSummaryResponse> getRentAdminSummariesByMemberId(final Long memberId) {
        return rentRepository.findRentAdminSummaries(memberId);
    }

    public RentAdminDetailResponse getRentAdminDetail(
            final Long memberId,
            final LocalDate boardingDate,
            final Long rentId
    ) {
        return new RentAdminDetailResponse(
                rentRepository.findRentAdminSummary(memberId, boardingDate, rentId)
                        .orElseThrow(RentNotFoundException::new),
                rentRepository.findRentJoinCount(memberId, boardingDate, rentId)
                        .orElse(RentJoinCountResponse.EMPTY),
                rentRepository.findRentJoinDetails(memberId, rentId, boardingDate)
        );
    }
}
