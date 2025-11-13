package likelion.lionboys.domain.image.controller;

import jakarta.validation.Valid;
import likelion.lionboys.domain.image.dto.request.ImageConfirmReq;
import likelion.lionboys.domain.image.dto.request.PresignedUrlReq;
import likelion.lionboys.domain.image.dto.response.ImageConfirmResp;
import likelion.lionboys.domain.image.dto.response.PresignedUrlResp;
import likelion.lionboys.domain.image.service.ImageService;
import likelion.lionboys.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/image")
@RequiredArgsConstructor
@RestController
public class ImageController {

    private final ImageService imageService;

    @GetMapping
    public ApiResponse<PresignedUrlResp> getImageUrls(PresignedUrlReq req) {
        return ApiResponse.success(imageService.getPutUrls(req));
    }

    @PostMapping("/confirm")
    public ApiResponse<ImageConfirmResp> confirm(
            @RequestBody @Valid ImageConfirmReq req
    ) {
        imageService.confirm(req);
        return ApiResponse.success(imageService.confirm(req));
    }
}
