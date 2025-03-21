package com.backend.allreva.rent.infra.rdb;

import com.backend.allreva.rent.command.domain.RentBoardingInfo;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RentBoardingInfoJpaRepository extends JpaRepository<RentBoardingInfo, Long> {

    Optional<RentBoardingInfo> findByRentIdAndDate(Long rentId, LocalDate date);

    @Modifying
    @Query("DELETE FROM RentBoardingInfo rfbd WHERE rfbd.rent.id = :rentId")
    void deleteAllByRentId(Long rentId);
}
