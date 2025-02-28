package com.backend.allreva.rent.fake;

import com.backend.allreva.rent_join.command.domain.RentJoin;
import com.backend.allreva.rent_join.command.domain.RentJoinRepository;
import com.backend.allreva.rent_join.query.response.RentJoinResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.test.util.ReflectionTestUtils;

public class RentJoinFakeRepository implements RentJoinRepository {

    private final AtomicLong rentJoinId = new AtomicLong(0);
    private final List<RentJoin> rentJoinTable = Collections.synchronizedList(new ArrayList<>());

    @Override
    public Optional<RentJoin> findById(final Long id) {
        return rentJoinTable.stream()
                .filter(rentJoin -> Objects.equals(rentJoin.getId(), id))
                .findFirst();
    }

    @Override
    public Integer countRentJoin(final Long rentId, final LocalDate boardingDate) {
        return rentJoinTable.stream()
                .filter(rentJoin -> Objects.equals(rentJoin.getRentId(), rentId))
                .filter(rentJoin -> Objects.equals(rentJoin.getBoardingDate(), boardingDate))
                .map(RentJoin::getPassengerNum)
                .reduce(Integer::sum)
                .orElse(0);
    }

    @Override
    public boolean existsByBoardingDateAndRentIdAndMemberId(
            final LocalDate boardingDate,
            final Long rentId,
            final Long memberId
    ) {
        return rentJoinTable.stream()
                .anyMatch(rentJoin -> Objects.equals(rentJoin.getBoardingDate(), boardingDate)
                        && Objects.equals(rentJoin.getRentId(), rentId)
                        && Objects.equals(rentJoin.getMemberId(), memberId));
    }

    @Override
    public RentJoin save(final RentJoin rentJoin) {
        if (rentJoin.getId() == null || rentJoin.getId() == 0L) {
            Long id = rentJoinId.incrementAndGet();
            ReflectionTestUtils.setField(rentJoin, "id", id);
            rentJoinTable.add(rentJoin);
            return rentJoin;
        }

        rentJoinTable.removeIf(o -> Objects.equals(o.getId(), rentJoin.getId()));
        rentJoinTable.add(rentJoin);
        return rentJoin;
    }

    @Override
    public void delete(final RentJoin rentJoin) {
        rentJoinTable.removeIf(o -> Objects.equals(o.getId(), rentJoin.getId()));
    }

    // for query, so not implemented in test
    @Override
    public List<RentJoinResponse> findRentJoin(final Long memberId) {
        return List.of();
    }
}
