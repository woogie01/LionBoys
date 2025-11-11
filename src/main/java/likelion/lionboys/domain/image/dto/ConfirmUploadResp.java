package likelion.lionboys.domain.image.dto;

public record ConfirmUploadResp(
        Long imageId,

        String status,  // "COMPLETED"

        String imageUrl,  // 이미지 다운로드 URL (프레사인드 GET URL 또는 CloudFront URL)

        Long fileSize,  // 실제 업로드된 파일 크기

        String s3Key  // S3 객체 키
) {
}
