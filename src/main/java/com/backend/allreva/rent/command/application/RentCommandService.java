package com.backend.allreva.rent.command.application;

import com.backend.allreva.common.application.S3ImageService;
import com.backend.allreva.common.event.Events;
import com.backend.allreva.rent.command.application.request.RentIdRequest;
import com.backend.allreva.rent.command.application.request.RentRegisterRequest;
import com.backend.allreva.rent.command.application.request.RentUpdateRequest;
import com.backend.allreva.rent.command.domain.Rent;
import com.backend.allreva.rent.command.domain.RentClosedEvent;
import com.backend.allreva.rent.command.domain.RentRepository;
import com.backend.allreva.rent.command.domain.RentSaveEvent;
import com.backend.allreva.rent.exception.RentNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RentCommandService {

    private final RentRepository rentRepository;
    private final S3ImageService s3ImageService;

    public Long registerRent(
            final RentRegisterRequest rentRegisterRequest,
            final Long memberId
    ) {
        Rent rent = rentRegisterRequest.toEntity(memberId);
        Rent savedRent = rentRepository.save(rent);
        Events.raise(new RentSaveEvent(savedRent));
        return savedRent.getId();
    }

    public void updateRent(
            final RentUpdateRequest rentUpdateRequest,
            final Long memberId
    ) {
        Rent rent = rentRepository.findById(rentUpdateRequest.rentId())
                .orElseThrow(RentNotFoundException::new);

        rent.validateMine(memberId);

        rentRepository.deleteBoardingDateAllByRentId(rentUpdateRequest.rentId());
        rent.updateRent(rentUpdateRequest);
    }

    public void closeRent(
            final RentIdRequest rentIdRequest,
            final Long memberId
    ) {
        Rent rent = rentRepository.findById(rentIdRequest.rentId())
                .orElseThrow(RentNotFoundException::new);

        rent.validateMine(memberId);
        rent.close();
    }

    @Async
    @TransactionalEventListener
    public void closeRent(RentClosedEvent event) {
        Rent rent = rentRepository.findById(event.getRentId())
                .orElseThrow(RentNotFoundException::new);

        rent.close();
    }

    public void deleteRent(
            final RentIdRequest rentIdRequest,
            final Long memberId
    ) {
        Rent rent = rentRepository.findById(rentIdRequest.rentId())
                .orElseThrow(RentNotFoundException::new);

        s3ImageService.delete(rent.getDetailInfo().getImage().getUrl());
        rent.validateMine(memberId);
        rentRepository.delete(rent);
    }
}
