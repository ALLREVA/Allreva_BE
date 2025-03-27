package com.backend.allreva.rent_join.infra;

import com.backend.allreva.rent_join.command.domain.RentJoin;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentJoinJpaRepository extends JpaRepository<RentJoin, Long> {

    List<RentJoin> findByRentIdAndBoardingDate(Long rentId, LocalDate boardingDate);

    boolean existsByMemberIdAndRentIdAndBoardingDate(Long memberId, Long rentId, LocalDate boardingDate);
}
