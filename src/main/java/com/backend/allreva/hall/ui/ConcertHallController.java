package com.backend.allreva.hall.ui;

import com.backend.allreva.common.dto.Response;
import com.backend.allreva.hall.query.application.ConcertHallQueryService;
import com.backend.allreva.hall.query.application.response.ConcertHallDetailResponse;
import com.backend.allreva.hall.query.application.response.ConcertHallMainResponse;
import com.backend.allreva.hall.query.application.response.RelatedConcertResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@RequestMapping("/api/v1/concert-halls")
@RestController
public class ConcertHallController implements ConcertHallControllerSwagger {

    private final ConcertHallQueryService concertHallQueryService;

    @Override
    public Response<ConcertHallDetailResponse> findHallDetailByHallCode(final String hallCode) {
        ConcertHallDetailResponse details = concertHallQueryService.findDetailByHallCode(hallCode);
        return Response.onSuccess(details);
    }

    @Override
    public Response<ConcertHallMainResponse> getConcertHallList(
            final String address,
            final int seatScale,
            final int pageSize,
            final String searchAfter1,
            final String searchAfter2,
            final String searchAfter3
    ) {
        List<Object> searchAfter = Stream.of(searchAfter1, searchAfter2, searchAfter3)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        ConcertHallMainResponse concertHallMain = concertHallQueryService.getConcertHallMain(
                address,
                seatScale,
                searchAfter,
                pageSize
        );
        return Response.onSuccess(concertHallMain);
    }

    @Override
    public Response<List<RelatedConcertResponse>> findRelatedConcertList(
            final String hallCode,
            final Long lastId,
            final int pageSize) {
        return Response.onSuccess(
                concertHallQueryService.getRelatedConcert(hallCode, lastId, pageSize)
        );
    }
}
