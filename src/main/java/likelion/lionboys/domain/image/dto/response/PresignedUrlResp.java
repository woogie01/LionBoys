package likelion.lionboys.domain.image.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record PresignedUrlResp(
        @NotEmpty
        List<PresignedUrlInfo> urls
) {
    public record PresignedUrlInfo(
            @NotNull
            Long imageId,  // DB에 저장된 PENDING 상태 Image ID

            @NotBlank
            String presignedUrl,  // 실제 업로드할 S3 URL

            @NotBlank
            String s3Key,  // "images/123/uuid.jpg"

            @NotNull
            Integer expiresInSeconds,  // 600 (10분)

            @NotNull
            LocalDateTime expiresAt,  // "2025-11-15T15:30:00"

            String originalFileName  // "내 사진.jpg" (매칭용)
    ) {}

    public static PresignedUrlResp of(List<PresignedUrlInfo> urls) {
        return new PresignedUrlResp(urls);
    }
}