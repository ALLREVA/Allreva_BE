package com.backend.allreva.rent_join.command.domain;

import com.backend.allreva.rent_join.query.response.RentJoinResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RentJoinRepository {

    Optional<RentJoin> findById(Long id);
    List<RentJoin> findByRentIdAndBoardingDate(Long rentId, LocalDate boardingDate);
    boolean exists(Long memberId, Long rentId, LocalDate boardingDate);
    RentJoin save(RentJoin rentJoin);
    void delete(RentJoin rentJoin);

    List<RentJoinResponse> findByMemberId(Long memberId);
}
