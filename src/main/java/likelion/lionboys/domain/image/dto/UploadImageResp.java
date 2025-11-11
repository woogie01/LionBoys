package likelion.lionboys.domain.image.dto;

// presigned URL 응답 DTO
public record UploadImageResp(
        Long imageId,
        String presignedUrl,
        Integer expiresIn,
        String s3Key
) {

}