package likelion.lionboys.global.infra.s3.dto;

// presigned URL 응답 DTO
public record PresignedUrlResp(
        String presignedUrl,
        Integer expiresIn,
        String method,
        String s3Key
) {
}