package likelion.lionboys.global.infra.s3.controller;

import jakarta.validation.Valid;
import likelion.lionboys.domain.image.dto.ConfirmUploadReq;
import likelion.lionboys.domain.image.dto.ConfirmUploadResp;
import likelion.lionboys.global.infra.s3.dto.PresignedUrlReq;
import likelion.lionboys.global.infra.s3.dto.PresignedUrlResp;
import likelion.lionboys.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/images")
@RestController
public class S3Controller {

    @PostMapping("/presigned-url")
    ApiResponse<PresignedUrlResp> upload(
            @RequestBody @Valid PresignedUrlReq req
    ) {
        return null;
    }

    @PostMapping("/confirm")
    ApiResponse<ConfirmUploadResp> confirm(
            @RequestBody @Valid ConfirmUploadReq req
            ) {
        return null;
    }
}
