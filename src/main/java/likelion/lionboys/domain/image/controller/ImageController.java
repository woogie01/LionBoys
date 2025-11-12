package likelion.lionboys.domain.image.controller;

import jakarta.validation.Valid;
import likelion.lionboys.domain.image.dto.ConfirmUploadReq;
import likelion.lionboys.domain.image.dto.ConfirmUploadResp;
import likelion.lionboys.domain.image.dto.UploadImageReq;
import likelion.lionboys.domain.image.dto.UploadImageResp;
import likelion.lionboys.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/images")
@RestController
public class ImageController {

    @PostMapping("/presigned-url")
    ApiResponse<UploadImageResp> upload(
            @RequestBody @Valid UploadImageReq req
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
