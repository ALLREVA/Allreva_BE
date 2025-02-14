package com.backend.allreva.rent_join.command.application;

import com.backend.allreva.common.event.Events;
import com.backend.allreva.rent.command.domain.Rent;
import com.backend.allreva.rent.command.domain.RentClosedEvent;
import com.backend.allreva.rent.command.domain.RentRepository;
import com.backend.allreva.rent.exception.RentNotFoundException;
import com.backend.allreva.rent_join.command.application.request.RentJoinApplyRequest;
import com.backend.allreva.rent_join.command.application.request.RentJoinIdRequest;
import com.backend.allreva.rent_join.command.application.request.RentJoinUpdateRequest;
import com.backend.allreva.rent_join.command.domain.RentJoin;
import com.backend.allreva.rent_join.command.domain.RentJoinRepository;
import com.backend.allreva.rent_join.exception.PassengersMaximumReachedException;
import com.backend.allreva.rent_join.exception.RentJoinAlreadyExistsException;
import com.backend.allreva.rent_join.exception.RentJoinNotFoundException;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class RentJoinCommandService {

    private final RentRepository rentRepository;
    private final RentJoinRepository rentJoinRepository;

    public Long applyRent(
            final RentJoinApplyRequest rentJoinApplyRequest,
            final Long memberId
    ) {
        // check if the user has already applied
        if (rentJoinRepository.existsByBoardingDateAndRentIdAndMemberId(
                rentJoinApplyRequest.boardingDate(),
                rentJoinApplyRequest.rentId(),
                memberId
        )) {
            throw new RentJoinAlreadyExistsException();
        }

        // if the number of passengers exceeds
        checkPassengersMaximumReached(
                rentJoinApplyRequest.rentId(),
                rentJoinApplyRequest.boardingDate(),
                rentJoinApplyRequest.passengerNum()
        );

        RentJoin rentJoin = rentJoinApplyRequest.toEntity(memberId);

        RentJoin savedRentJoin = rentJoinRepository.save(rentJoin);
        return savedRentJoin.getId();
    }

    public void updateRentJoin(
            final RentJoinUpdateRequest rentJoinUpdateRequest,
            final Long memberId
    ) {
        RentJoin rentJoin = rentJoinRepository.findById(rentJoinUpdateRequest.rentJoinId())
                .orElseThrow(RentJoinNotFoundException::new);

        rentJoin.validateMine(memberId);
        rentJoin.updateRentJoin(rentJoinUpdateRequest);
    }

    public void deleteRentJoin(
            final RentJoinIdRequest rentJoinIdRequest,
            final Long memberId
    ) {
        RentJoin rentJoin = rentJoinRepository.findById(rentJoinIdRequest.rentJoinId())
                .orElseThrow(RentJoinNotFoundException::new);

        rentJoin.validateMine(memberId);
        rentJoinRepository.delete(rentJoin);
    }

    private void checkPassengersMaximumReached(
            final Long rentId,
            final LocalDate boardingDate,
            final Integer passengerNum
    ) {
        Rent rent = rentRepository.findById(rentId)
                .orElseThrow(RentNotFoundException::new);
        int maximumCount = rent.getAdditionalInfo().getRecruitmentCount();

        Integer currentPassengerCount = rentJoinRepository.countRentJoin(rentId, boardingDate);
        if (currentPassengerCount + passengerNum > maximumCount) {
            throw new PassengersMaximumReachedException();
        }
        // rent close event
        if (currentPassengerCount + passengerNum == maximumCount) {
            Events.raise(new RentClosedEvent(rentId));
        }
    }
}
