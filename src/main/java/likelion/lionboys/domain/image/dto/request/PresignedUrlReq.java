package likelion.lionboys.domain.image.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

public record PresignedUrlReq(
        @NotNull(message = "roundId는 필수입니다")
        Long roundId,

        @NotEmpty(message = "최소 1개 이상의 이미지 정보가 필요합니다")
        @Size(max = 50, message = "한 번에 최대 50개까지 업로드 가능합니다")
        @Valid
        List<ImageUploadInfo> images
) {
    public record ImageUploadInfo(
            @NotBlank(message = "Content-Type은 필수입니다")
            @Pattern(
                    regexp = "^image/(jpeg|jpg|png)$", // "image/jpeg", "image/jpg", "image/png"만 허용
                    message = "지원하지 않는 이미지 형식입니다"
            )
            String contentType, // ⬅️ "image/jpeg" 같은 문자열을 받음

            @NotNull(message = "파일 크기는 필수입니다")
            @Min(value = 1, message = "파일 크기는 1바이트 이상이어야 합니다")
            @Max(value = 10485760, message = "파일 크기는 10MB 이하여야 합니다")
            Long fileSize,

            @Size(max = 255, message = "파일명은 255자 이하여야 합니다")
            String originalFileName
    ) {}
}