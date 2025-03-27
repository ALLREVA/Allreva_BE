package com.backend.allreva.rent.infra.rdb;

import com.backend.allreva.rent.command.domain.Rent;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentJpaRepository extends JpaRepository<Rent, Long> {

    Optional<Rent> findByIdAndMemberId(Long id, Long memberId);
}
