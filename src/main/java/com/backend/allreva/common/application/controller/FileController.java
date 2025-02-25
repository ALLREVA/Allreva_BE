package com.backend.allreva.common.application.controller;

import com.backend.allreva.auth.security.AuthMember;
import com.backend.allreva.common.application.FileService;
import com.backend.allreva.common.application.dto.GetPresignedUrlRequest;
import com.backend.allreva.common.dto.Response;
import com.backend.allreva.member.command.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/file")
@RequiredArgsConstructor
public class FileController implements FileControllerSwagger{
    private final FileService fileService;

    @PostMapping("/presigned-url")
    @Override
    public Response<String> getPresignedUrl(
            @RequestBody final GetPresignedUrlRequest request,
            @AuthMember final Member member
    ) {
        String preSignedUrl = fileService.getPreSignedUrl(request, member);
        return Response.onSuccess(preSignedUrl);
    }
    @DeleteMapping("/presigned-url")
    @Override
    public Response<String> getDeletedPresignedUrl(
            @RequestParam final String url
    ){
        String deletePreSignedUrl = fileService.getDeletePreSignedUrl(url);

        return Response.onSuccess(deletePreSignedUrl);
    }
}
