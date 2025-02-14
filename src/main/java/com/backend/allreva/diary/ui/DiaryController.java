package com.backend.allreva.diary.ui;

import com.backend.allreva.auth.security.AuthMember;
import com.backend.allreva.common.dto.Response;
import com.backend.allreva.common.model.Image;
import com.backend.allreva.diary.command.application.DiaryCommandService;
import com.backend.allreva.diary.command.application.request.AddDiaryRequest;
import com.backend.allreva.diary.command.application.request.UpdateDiaryRequest;
import com.backend.allreva.diary.query.DiaryQueryService;
import com.backend.allreva.diary.query.response.DiaryDetailResponse;
import com.backend.allreva.diary.query.response.DiarySummaryResponse;
import com.backend.allreva.member.command.domain.Member;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/diaries")
@RestController
public class DiaryController {

    private final DiaryCommandService diaryCommandService;
    private final DiaryQueryService diaryQueryService;

    @Operation(summary = "공연 기록 등록", description = "이미지 URL 만 넣어주세요")
    @PostMapping
    public Response<Long> addDiary(
            @RequestBody final AddDiaryRequest request,
            @RequestBody final List<Image> images,
            @AuthMember final Member member
    ) {
        Long diaryId = diaryCommandService.add(request, images, member.getId());
        return Response.onSuccess(diaryId);
    }

    @Operation(summary = "공연 기록 수정", description = "이미지 기존 이미지중에 삭제 된거는 직접 삭제후 유지되는 이미지 + 추가 이미지")
    @PatchMapping
    public Response<Void> updateDiary(
            @RequestBody final UpdateDiaryRequest request,
            @RequestBody final List<Image> images,
            @AuthMember final Member member
    ) {
        diaryCommandService.update(request, images, member.getId());
        return Response.onSuccess();
    }

    @Operation(summary = "공연 기록 상세 조회", description = "공연 기록 상세 조회 API")
    @GetMapping("/{diaryId}")
    public Response<DiaryDetailResponse> findDiaryDetail(
            @PathVariable("diaryId") final Long diaryId,
            @AuthMember final Member member
    ) {
        DiaryDetailResponse detail = diaryQueryService.findDetailById(diaryId, member.getId());
        return Response.onSuccess(detail);
    }

    @Operation(summary = "공연 기록 목록 조회", description = "공연 기록 목록 조회 API")
    @GetMapping("/list")
    public Response<List<DiarySummaryResponse>> findSummaries(
            @RequestParam(name = "year") final int year,
            @RequestParam(name = "month") final int month,
            @AuthMember final Member member
    ) {
        List<DiarySummaryResponse> summaries = diaryQueryService.findSummaries(
                member.getId(),
                year,
                month
        );
        return Response.onSuccess(summaries);
    }


    @Operation(summary = "공연 기록 삭제", description = "공연 기록 삭제 API")
    @DeleteMapping("/{diaryId}")
    public Response<Void> deleteDiary(
            @PathVariable("diaryId") final Long diaryId,
            @AuthMember final Member member
    ) {
        diaryCommandService.delete(diaryId, member.getId());
        return Response.onSuccess();
    }
}
