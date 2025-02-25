package com.backend.allreva.rent.fake;

import com.backend.allreva.rent.command.domain.Rent;
import com.backend.allreva.rent.command.domain.RentBoardingDate;
import com.backend.allreva.rent.command.domain.RentRepository;
import com.backend.allreva.rent.command.domain.value.Region;
import com.backend.allreva.rent.query.application.response.DepositAccountResponse;
import com.backend.allreva.rent.query.application.response.RentAdminSummaryResponse;
import com.backend.allreva.rent.query.application.response.RentDetailResponse;
import com.backend.allreva.rent.query.application.response.RentJoinCountResponse;
import com.backend.allreva.rent.query.application.response.RentJoinDetailResponse;
import com.backend.allreva.rent.query.application.response.RentSummaryResponse;
import com.backend.allreva.survey.query.application.response.SortType;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.test.util.ReflectionTestUtils;

public class RentFakeRepository implements RentRepository {

    private AtomicLong rentId = new AtomicLong(0);
    private final List<Rent> rentTable = Collections.synchronizedList(new ArrayList<>());
    
    @Override
    public Optional<Rent> findById(Long id) {
        return rentTable.stream()
                .filter(rent -> Objects.equals(rent.getId(), id))
                .findFirst();
    }

    @Override
    public boolean existsById(Long id) {
        return rentTable.stream()
                .anyMatch(rent -> Objects.equals(rent.getId() - 1, id));
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
    public List<RentBoardingDate> updateRentBoardingDates(Long rentId, List<RentBoardingDate> rentBoardingDates) {
        rentTable.stream()
                .filter(o -> Objects.equals(o.getId(), rentId))
                .findFirst()
                .ifPresent(rent -> ReflectionTestUtils.setField(rent, "rentBoardingDates", rentBoardingDates));
        return rentBoardingDates;
    }

    @Override
    public void deleteBoardingDateAllByRentId(Long rentId) {
        rentTable.stream()
               .filter(o -> Objects.equals(o.getId(), rentId))
               .findFirst()
               .ifPresent(rent -> ReflectionTestUtils.setField(rent, "boardingDates", new ArrayList<>()));
    }

    // soft delete, but this is not used in test
    @Override
    public void delete(Rent rent) {
        rentTable.removeIf(o -> Objects.equals(o.getId(), rent.getId()));
    }

    @Override
    public List<RentSummaryResponse> findRentSummaries(Region region, SortType sortType, LocalDate lastEndDate, Long lastId, int pageSize) {
        return null;
    }

    @Override
    public Optional<RentDetailResponse> findRentDetailById(Long rentId) {
        return null;
    }

    @Override
    public Optional<DepositAccountResponse> findDepositAccountById(Long rentId) {
        return null;
    }

    @Override
    public List<RentAdminSummaryResponse> findRentAdminSummaries(Long memberId) {
        return null;
    }

    @Override
    public Optional<RentAdminSummaryResponse> findRentAdminSummary(Long memberId, LocalDate boardingDate, Long rentId) {
        return null;
    }

    @Override
    public Optional<RentJoinCountResponse> findRentJoinCount(Long memberId, LocalDate boardingDate, Long rentId) {
        return null;
    }

    @Override
    public List<RentJoinDetailResponse> findRentJoinDetails(Long memberId, Long rentId, LocalDate boardingDate) {
        return null;
    }

    @Override
    public List<RentSummaryResponse> findRentMainSummaries() {
        return null;
    }
}
