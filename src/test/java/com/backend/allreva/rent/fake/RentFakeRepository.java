package com.backend.allreva.rent.fake;

import com.backend.allreva.rent.command.domain.Rent;
import com.backend.allreva.rent.command.domain.RentBoardingInfo;
import com.backend.allreva.rent.command.domain.RentRepository;
import com.backend.allreva.rent.command.domain.value.Region;
import com.backend.allreva.rent.query.application.response.RentAdminSummaryResponse;
import com.backend.allreva.rent.query.application.response.RentDetailResponse;
import com.backend.allreva.rent.query.application.response.RentSummaryResponse;
import com.backend.allreva.rent_join.query.response.RentJoinCountResponse;
import com.backend.allreva.survey.query.application.response.SortType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.test.util.ReflectionTestUtils;

public class RentFakeRepository implements RentRepository {

    private final AtomicLong rentId = new AtomicLong(0);
    private final List<Rent> rentTable = Collections.synchronizedList(new ArrayList<>());
    
    @Override
    public Optional<Rent> findById(Long id) {
        return rentTable.stream()
                .filter(rent -> Objects.equals(rent.getId(), id))
                .findFirst();
    }

    @Override
    public Optional<Rent> findByIdAndMemberId(final Long rentId, final Long memberId) {
        return rentTable.stream()
                .filter(rent -> Objects.equals(rent.getId(), rentId))
                .filter(rent -> Objects.equals(rent.getMemberId(), memberId))
                .findFirst();
    }

    @Override
    public Optional<RentBoardingInfo> findByIdAndBoardingDate(final Long rentId, final LocalDate date) {
        Optional<Rent> rentOptional = rentTable.stream()
                .filter(rent -> Objects.equals(rent.getId(), rentId))
                .findFirst();
        if (rentOptional.isEmpty()) {
            return Optional.empty();
        }
        List<RentBoardingInfo> boardingInfos = rentOptional.get().getBoardingInfos();
        return boardingInfos.stream()
                .filter(bi -> Objects.equals(bi.getDate(), date))
                .findFirst();
    }

    @Override
    public Rent save(Rent rent) {
        if (rent.getId() == null || rent.getId() == 0L) {
            Long id = rentId.incrementAndGet();
            ReflectionTestUtils.setField(rent, "id", id);
            rentTable.add(rent);
            return rent;
        }

        rentTable.removeIf(o -> Objects.equals(o.getId(), rent.getId()));
        rentTable.add(rent);
        return rent;
    }

    @Override
    public void deleteBoardingInfoAllByRentId(Long rentId) {
        rentTable.stream()
               .filter(o -> Objects.equals(o.getId(), rentId))
               .findFirst()
               .ifPresent(rent -> ReflectionTestUtils.setField(rent, "boardingInfos", new ArrayList<>()));
    }

    @Override
    public void delete(Rent rent) {
        rentTable.removeIf(o -> Objects.equals(o.getId(), rent.getId()));
    }

    // for query, so not implemented in test
    @Override
    public List<RentSummaryResponse> findRentSummaries(Region region, SortType sortType, LocalDate lastEndDate, Long lastId, int pageSize) {
        return null;
    }

    @Override
    public Optional<RentDetailResponse> findRentDetail(Long rentId) {
        return null;
    }

    @Override
    public List<RentAdminSummaryResponse> findRentAdminSummaries(final Long memberId, final Long lastId,
            final int pageSize) {
        return null;
    }

    @Override
    public Optional<RentJoinCountResponse> findRentJoinCount(Long memberId, LocalDate boardingDate, Long rentId) {
        return null;
    }
}
