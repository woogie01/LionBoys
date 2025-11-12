package likelion.lionboys.global.infra.s3.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PresignedUrlReq(
        @NotNull(message = "roundId는 필수")
        Long roundId,

    @NotBlank(message = "Content-Type은 필수입니다")
    String contentType
) {
}
