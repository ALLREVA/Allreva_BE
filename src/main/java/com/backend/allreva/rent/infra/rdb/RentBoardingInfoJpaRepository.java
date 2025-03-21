package com.backend.allreva.rent.infra.rdb;

import com.backend.allreva.rent.command.domain.RentBoardingInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RentBoardingInfoJpaRepository extends JpaRepository<RentBoardingInfo, Long> {
    @Modifying
    @Query("DELETE FROM RentBoardingInfo rfbd WHERE rfbd.rent.id = :rentId")
    void deleteAllByRentId(Long rentId);
}
