package com.backend.allreva.rent_join.command.domain;

import com.backend.allreva.rent_join.query.response.RentJoinResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RentJoinRepository {

    Optional<RentJoin> findById(Long id);

    Integer countRentJoin(Long rentId, LocalDate boardingDate);

    boolean existsById(Long id);

    boolean existsByBoardingDateAndRentIdAndMemberId(LocalDate boardingDate, Long rentId, Long memberId);

    RentJoin save(RentJoin rentJoin);

    void delete(RentJoin rentJoin);

    List<RentJoinResponse> findRentJoin(Long memberId);
}
