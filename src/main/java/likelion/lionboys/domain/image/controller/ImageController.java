package likelion.lionboys.domain.image.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import likelion.lionboys.domain.image.dto.request.ImageConfirmReq;
import likelion.lionboys.domain.image.dto.request.ImageGetReq;
import likelion.lionboys.domain.image.dto.request.PresignedUrlReq;
import likelion.lionboys.domain.image.dto.response.ImageConfirmResp;
import likelion.lionboys.domain.image.dto.response.ImageGetResp;
import likelion.lionboys.domain.image.dto.response.PresignedUrlResp;
import likelion.lionboys.domain.image.service.ImageService;
import likelion.lionboys.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Image API", description = "회식 사진 업로드 및 조회 API")
@RequestMapping("/api/images")
@RequiredArgsConstructor
@RestController
public class ImageController {

    private final ImageService imageService;

    @Operation(
            summary = "이미지 업로드 PresignedUrl 발급" +
            "발급받은 URL로 클라이언트가 직접 S3에 업로드한 후, confirm API를 호출해야 합니다."
    )
    @GetMapping("/presigned-url")
    public ApiResponse<PresignedUrlResp> getImagePresignedUrls(PresignedUrlReq req) {
        return ApiResponse.success(imageService.getPutUrls(req));
    }

    @Operation(
            summary = "이미지 업로드 확인",
            description = "S3에 이미지 업로드가 완료되었음을 서버에 알립니다. " +
                    "업로드 성공 여부에 따라 이미지 상태를 COMPLETED 또는 FAILED로 변경합니다."
    )
    @PostMapping("/confirm")
    public ApiResponse<ImageConfirmResp> confirmUpload(
            @RequestBody @Valid ImageConfirmReq req
    ) {
        imageService.confirm(req);
        return ApiResponse.success(imageService.confirm(req));
    }

    @Operation(
            summary = "이미지 다운로드 URL 조회",
            description = "특정 회식(Round)의 업로드된 이미지들의 다운로드 URL을 조회합니다. " +
                    "반환된 Presigned URL은 10분간 유효합니다."
    )
    @GetMapping
    public ApiResponse<ImageGetResp> getDownloadUrls(

            @RequestBody ImageGetReq req
    ) {
        return ApiResponse.success(imageService.getDownloadUrls(req));
    }
}
