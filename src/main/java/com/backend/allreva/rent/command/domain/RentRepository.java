package com.backend.allreva.rent.command.domain;

import com.backend.allreva.rent.command.domain.value.Region;
import com.backend.allreva.rent.query.application.response.RentAdminSummaryResponse;
import com.backend.allreva.rent.query.application.response.RentDetailResponse;
import com.backend.allreva.rent.query.application.response.RentSummaryResponse;
import com.backend.allreva.rent_join.query.response.RentJoinCountResponse;
import com.backend.allreva.survey.query.application.response.SortType;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RentRepository {

    // for command
    Optional<Rent> findById(Long id);
    Optional<Rent> findByIdAndMemberId(Long rentId, Long memberId);
    Optional<RentBoardingInfo> findByIdAndBoardingDate(Long rentId, LocalDate date);
    Rent save(Rent rent);
    void deleteBoardingInfoAllByRentId(Long rentId);
    void delete(Rent rent);

    // for query
    List<RentSummaryResponse> findRentSummaries(Region region, SortType sortType, LocalDate lastEndDate, Long lastId, int pageSize);
    Optional<RentDetailResponse> findRentDetail(Long rentId);
    List<RentAdminSummaryResponse> findRentAdminSummaries(Long memberId, Long lastId, int pageSize);
    Optional<RentJoinCountResponse> findRentJoinCount(Long memberId, LocalDate boardingDate, Long rentId);
}
