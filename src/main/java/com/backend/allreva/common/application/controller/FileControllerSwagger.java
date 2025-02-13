package com.backend.allreva.common.application.controller;

import com.backend.allreva.auth.security.AuthMember;
import com.backend.allreva.common.application.dto.GetPresignedUrlRequest;
import com.backend.allreva.common.dto.Response;
import com.backend.allreva.member.command.domain.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "S3 preSigned url 요청 API ")
public interface FileControllerSwagger {
    @PostMapping("/presigned-url")
    @Operation(summary = "이미지 저장 URL 요청", description = "파일명과 , PROFILE, CHAT, REVIEW, SURVEY, RENT 이중 파일 타입 선택")
    Response<String> getPresignedUrl(
            @RequestBody GetPresignedUrlRequest request,
            @AuthMember Member member
    );

    @Operation(summary = "이미지 삭제 요청 URL", description = "삭제할 이미지 URL")
    @DeleteMapping("/presigned-url")
    Response<String> getDeletedPresignedUrl(
            @RequestParam String url
    );
}
