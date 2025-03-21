package com.backend.allreva.rent_join.query;

import com.backend.allreva.rent_join.command.domain.RentJoinRepository;
import com.backend.allreva.rent_join.query.response.RentJoinResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RentJoinQueryService {

    private final RentJoinRepository rentJoinRepository;

    public List<RentJoinResponse> getRentJoin(final Long memberId) {
        return rentJoinRepository.findByMemberId(memberId);
    }
}
