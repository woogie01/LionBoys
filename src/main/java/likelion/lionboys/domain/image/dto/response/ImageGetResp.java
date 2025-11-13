package likelion.lionboys.domain.image.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import likelion.lionboys.domain.image.entity.ContentType;

import java.time.LocalDateTime;
import java.util.List;

public record ImageGetResp(
        @NotNull
        Long roundId,

        @NotEmpty
        List<ImageInfo> images
) {
    public record ImageInfo(
            @NotNull
            Long imageId,

            @NotBlank
            String downloadUrl,  // Presigned GET URL

            @NotBlank
            String s3Key,

            String originalFileName,

            @NotNull
            Long fileSize,

            @NotNull
            ContentType contentType,

            @NotNull
            LocalDateTime uploadedAt,

            @NotNull
            Integer urlExpiresIn  // 600 (10분)
    ) {}

    public static ImageGetResp of(Long roundId, List<ImageInfo> images) {
        return new ImageGetResp(roundId, images);
    }
}