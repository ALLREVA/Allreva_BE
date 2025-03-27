package com.backend.allreva.rent_join.infra;

import com.backend.allreva.rent_join.command.domain.RentJoin;
import com.backend.allreva.rent_join.command.domain.RentJoinRepository;
import com.backend.allreva.rent_join.query.response.RentJoinResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RentJoinRepositoryImpl implements RentJoinRepository {

    private final RentJoinJpaRepository rentJoinJpaRepository;
    private final RentJoinDslRepository rentJoinDslRepository;

    @Override
    public Optional<RentJoin> findById(final Long id) {
        return rentJoinJpaRepository.findById(id);
    }

    @Override
    public List<RentJoin> findByRentIdAndBoardingDate(final Long rentId, final LocalDate boardingDate) {
        return rentJoinJpaRepository.findByRentIdAndBoardingDate(rentId, boardingDate);
    }

    @Override
    public boolean exists(
            final Long memberId,
            final Long rentId,
            final LocalDate boardingDate
    ) {
        return rentJoinJpaRepository.existsByMemberIdAndRentIdAndBoardingDate(memberId, rentId, boardingDate);
    }

    @Override
    public RentJoin save(final RentJoin rentJoin) {
        return rentJoinJpaRepository.save(rentJoin);
    }

    @Override
    public void delete(final RentJoin rentJoin) {
        rentJoinJpaRepository.delete(rentJoin);
    }

    @Override
    public List<RentJoinResponse> findByMemberId(final Long memberId) {
        return rentJoinDslRepository.findByMemberId(memberId);
    }
}
