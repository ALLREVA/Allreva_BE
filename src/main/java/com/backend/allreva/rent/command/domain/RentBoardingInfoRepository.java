package com.backend.allreva.rent.command.domain;

import java.util.List;

public interface RentBoardingInfoRepository {
    List<RentBoardingInfo> findByRentId(Long rentId);
}
