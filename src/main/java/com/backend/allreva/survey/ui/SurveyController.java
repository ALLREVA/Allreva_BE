package com.backend.allreva.survey.ui;

import com.backend.allreva.auth.security.AuthMember;
import com.backend.allreva.common.dto.Response;
import com.backend.allreva.member.command.domain.Member;
import com.backend.allreva.survey.command.application.SurveyCommandService;
import com.backend.allreva.survey.command.application.request.OpenSurveyRequest;
import com.backend.allreva.survey.command.application.request.SurveyIdRequest;
import com.backend.allreva.survey.command.application.request.UpdateSurveyRequest;
import com.backend.allreva.survey.command.domain.value.Region;
import com.backend.allreva.survey.exception.SurveyIllegalParameterException;
import com.backend.allreva.survey.query.application.SurveyQueryService;
import com.backend.allreva.survey.query.application.response.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/surveys")
@Tag(name = "수요조사 API Controller")
public class SurveyController {
    private final SurveyCommandService surveyCommandService;
    private final SurveyQueryService surveyQueryService;

    @Operation(summary = "수요조사 개설 API", description = "수요조사를 개설합니다.")
    @PostMapping
    public Response<Long> openSurvey(@AuthMember Member member,
                                     @Valid @RequestBody OpenSurveyRequest openSurveyRequest) {
        return Response.onSuccess(surveyCommandService.openSurvey(member.getId(), openSurveyRequest));
    }

    @Operation(summary = "수요조사 수정 API", description = "수요조사를 수정합니다.")
    @PatchMapping
    public Response<Void> updateSurvey(@AuthMember Member member,
                                       @Valid @RequestBody UpdateSurveyRequest updateSurveyRequest) {
        surveyCommandService.updateSurvey(member.getId(), updateSurveyRequest);
        return Response.onSuccess();
    }


    @Operation(summary = "수요조사 삭제 API", description = "수요조사를 삭제합니다.")
    @DeleteMapping
    public Response<Void> removeSurvey(@AuthMember Member member,
                                       @RequestBody SurveyIdRequest surveyIdRequest) {
        surveyCommandService.removeSurvey(member.getId(), surveyIdRequest);
        return Response.onSuccess();
    }

    @Operation(summary = "수요조사 상세 조회 API", description = "수요조사를 상세조회합니다.")
    @GetMapping("/{surveyId}")
    public Response<SurveyDetailResponse> findSurveyDetail(@PathVariable(name = "surveyId") Long surveyId) {
        return Response.onSuccess(surveyQueryService.findSurveyDetail(surveyId));
    }

    @Operation(
            summary = "수요조사 목록 조회 API",
            description = "수요조사 목록을 조회합니다.\n\n" +
                    "- <b>정렬 옵션</b>:\n" +
                    "  - 최신순 (<b>LATEST</b>): lastId를 마지막 항목의 ID로 전달. lastEndDate는 주지마세요.\n" +
                    "  - 오래된 순 (<b>OLDEST</b>): lastId를 마지막 항목의 ID로 전달. lastEndDate는 주지마세요.\n" +
                    "  - 마감 임박순 (<b>CLOSING</b>): 마지막 항목의 lastId와 lastEndDate를 전달하며, 둘 모두 필수입니다.\n\n" +
                    "- <b>첫 페이지 요청</b>: lastId와 lastEndDate를 전달하지 않으면 됩니다.\n" +
                    "- <b>기본값</b>:\n" +
                    "  - <b>sort</b>: 최신순 (LATEST)\n" +
                    "  - <b>pageSize</b>: 10\n" +
                    "  - <b>region</b>: 전체 조회"
    )
    @GetMapping("/list")
    public Response<List<SurveySummaryResponse>> findSurveyList(
            @RequestParam(name = "region", required = false) final Region region,
            @RequestParam(name = "sort", defaultValue = "LATEST") final SortType sortType,
            @RequestParam(name = "lastId", required = false) final Long lastId,
            @RequestParam(name = "lastEndDate", required = false) final LocalDate lastEndDate,
            @Min(10) @RequestParam(name = "pageSize", defaultValue = "10") final int pageSize
    ) {
        if (lastEndDate != null && lastId == null) {
            throw new SurveyIllegalParameterException();
        }

        List<SurveySummaryResponse> responses = surveyQueryService
                .findSurveyList(region, sortType, lastId, lastEndDate, pageSize);
        return Response.onSuccess(responses);
    }

    @GetMapping("/main")
    @Operation(
            summary = "첫 화면 survey API 입니다.",
            description = "첫 화면 survey API 입니다. 현재 날짜에서 가장 가까운 콘서트 순으로 5개 정렬"
    )
    public Response<List<SurveySummaryResponse>> findSurveyMainList(){
        return Response.onSuccess(surveyQueryService.findSurveyMainList());
    }
}
