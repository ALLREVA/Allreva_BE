package com.backend.allreva.common.application.dto;

import com.backend.allreva.common.application.FileType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GetPresignedUrlRequest(
        @NotBlank(message = "저장할 파일 이름은 필수입니다.")
        String fileName,
        @NotNull(message = "fileType은 필수 입니다.")
        FileType fileType

) {
}
