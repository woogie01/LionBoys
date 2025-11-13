package likelion.lionboys.global.infra.s3.dto;

import com.amazonaws.HttpMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record S3PresignedUrl(
        @NotBlank
        String url,

        @NotNull
        HttpMethod method,

        @NotNull
        Integer expiresInSeconds,  // 600 (10분)

        @NotNull
        LocalDateTime expiredAt
) {
    public static S3PresignedUrl of(
            String url,
            HttpMethod method,
            Integer expiresInSeconds,
            LocalDateTime expiredAt
    )
    {
        return new S3PresignedUrl(url, method, expiresInSeconds, expiredAt);
    }
}
