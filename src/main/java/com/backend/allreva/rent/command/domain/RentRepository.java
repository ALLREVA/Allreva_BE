package com.backend.allreva.rent.command.domain;

import com.backend.allreva.rent.command.domain.value.Region;
import com.backend.allreva.rent.query.application.response.DepositAccountResponse;
import com.backend.allreva.rent.query.application.response.RentAdminSummaryResponse;
import com.backend.allreva.rent.query.application.response.RentDetailResponse;
import com.backend.allreva.rent.query.application.response.RentJoinCountResponse;
import com.backend.allreva.rent.query.application.response.RentJoinDetailResponse;
import com.backend.allreva.rent.query.application.response.RentSummaryResponse;
import com.backend.allreva.survey.query.application.response.SortType;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RentRepository {

    // for command
    Optional<Rent> findById(Long id);
    boolean existsById(Long id);
    Rent save(Rent rent);
    List<RentBoardingDate> updateRentBoardingDates(Long rentId, List<RentBoardingDate> rentBoardingDates);
    void deleteBoardingDateAllByRentId(Long rentId);
    void delete(Rent rent);

    // for query
    List<RentSummaryResponse> findRentSummaries(Region region, SortType sortType, LocalDate lastEndDate, Long lastId, int pageSize);
    Optional<RentDetailResponse> findRentDetailById(Long rentId);
    Optional<DepositAccountResponse> findDepositAccountById(Long rentId);

    List<RentAdminSummaryResponse> findRentAdminSummaries(Long memberId);
    Optional<RentAdminSummaryResponse> findRentAdminSummary(Long memberId, LocalDate boardingDate, Long rentId);
    Optional<RentJoinCountResponse> findRentJoinCount(Long memberId, LocalDate boardingDate, Long rentId);
    List<RentJoinDetailResponse> findRentJoinDetails(Long memberId, Long rentId, LocalDate boardingDate);

    List<RentSummaryResponse> findRentMainSummaries();
}
