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

    /**
     * [Participate] 자신이 참여한 차 대절 조회
     * TODO: no offset and limit
     * @param memberId
     * @return 자신이 참여한 차 대절 목록
     */
    public List<RentJoinResponse> getRentJoin(final Long memberId) {
        return rentJoinRepository.findByMemberId(memberId);
    }
}
