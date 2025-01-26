package com.backend.allreva.hall.ui;

import com.backend.allreva.common.dto.Response;
import com.backend.allreva.hall.query.application.response.ConcertHallDetailResponse;
import com.backend.allreva.hall.query.application.response.ConcertHallMainResponse;
import com.backend.allreva.hall.query.application.response.RelatedConcertResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "공연장 API", description = "공연장 관련 API")
public interface ConcertHallControllerSwagger {

    @Operation(summary = "공연장 상세 조회", description = "공연장 상세 조회 API")
    @GetMapping("/{hallCode}")
    Response<ConcertHallDetailResponse> findHallDetailByHallCode(
            @PathVariable("hallCode") String hallCode
    );

    @Operation(
            summary = "공연장 메인 API",
            description = "searchAfter1, searchAfter2에 이전 SearchAfter에 있는 값들을 순서대로 넣어주어야 합니다."
    )
    @GetMapping("/list")
    Response<ConcertHallMainResponse> getConcertHallList(
            @RequestParam(defaultValue = "") String address,
            @RequestParam(defaultValue = "0") int seatScale,
            @RequestParam(defaultValue = "7") int pageSize,
            @RequestParam(required = false) String searchAfter1,
            @RequestParam(required = false) String searchAfter2,
            @RequestParam(required = false) String searchAfter3
    );

    @Operation(
            summary = "콘서트 홀에 관련 공연 API",
            description = "콘서트 홀에 관련 공연 API 무한 스크롤"
    )
    @GetMapping("/relate/list")
    Response<List<RelatedConcertResponse>> findRelatedConcertList(
            @RequestParam(required = true) String hallCode,
            @RequestParam(required = false) Long lastId,
            @RequestParam(defaultValue = "3") int pageSize
    );
}