package com.backend.allreva.hall.ui;

import com.backend.allreva.common.dto.Response;
import com.backend.allreva.hall.query.application.ConcertHallQueryService;
import com.backend.allreva.hall.query.application.response.ConcertHallDetailResponse;
import com.backend.allreva.hall.query.application.response.ConcertHallMainResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@RequestMapping("/api/v1/concert-halls")
@RestController
public class ConcertHallController {

    private final ConcertHallQueryService concertHallQueryService;

    @Operation(summary = "공연장 상세 조회", description = "공연장 상세 조회 API")
    @GetMapping("/{hallCode}")
    public Response<ConcertHallDetailResponse> findHallDetailByHallCode(
            @PathVariable("hallCode") final String hallCode
    ) {
        ConcertHallDetailResponse details = concertHallQueryService.findDetailByHallCode(hallCode);
        return Response.onSuccess(details);
    }

    @Operation(
            summary = "공연장 메인 api 입니다.",
            description = "searchAfter1, searchAfter2에 " +
                    "이전 SearchAfter에 있는 값들을 순서대로 넣어주어야 합니다."
    )
    @GetMapping("/list")
    public Response<ConcertHallMainResponse> getConcertHallList(
            @RequestParam(defaultValue = "") final String address,
            @RequestParam(defaultValue = "0") final int seatScale,
            @RequestParam(defaultValue = "7") final int pageSize,
            @RequestParam(required = false) final String searchAfter1,
            @RequestParam(required = false) final String searchAfter2,
            @RequestParam(required = false) final String searchAfter3
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
}
