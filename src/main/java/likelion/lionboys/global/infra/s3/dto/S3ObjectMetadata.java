package likelion.lionboys.global.infra.s3.dto;

import jakarta.validation.constraints.NotNull;
import likelion.lionboys.domain.image.entity.ContentType;

import java.util.Date;

public record S3ObjectMetadata(
        @NotNull
        Long contentLength,

        ContentType contentType,

        @NotNull
        Date lastModified
) {
    public static S3ObjectMetadata of(
            Long contentLength,
            ContentType contentType,
            Date lastModified
    )
    {
        return new S3ObjectMetadata(contentLength, contentType, lastModified);
    }

}
