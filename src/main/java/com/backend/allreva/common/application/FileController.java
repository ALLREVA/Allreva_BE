package com.backend.allreva.common.application;

import com.backend.allreva.common.application.dto.GetPresignedUrlRequest;
import com.backend.allreva.common.dto.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/avi/v1/file")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @PostMapping("/presigned-url")
    public Response<String> getPresignedUrl(@RequestBody GetPresignedUrlRequest request) {
        String preSignedUrl = fileService.getPreSignedUrl(request);
        return Response.onSuccess(preSignedUrl);
    }
}
