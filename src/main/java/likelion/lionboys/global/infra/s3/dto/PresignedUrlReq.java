package likelion.lionboys.global.infra.s3.dto;


import jakarta.validation.constraints.NotBlank;

public record PresignedUrlReq(
    @NotBlank(message = "Content-Type은 필수입니다")
    String contentType
) {
    public static PresignedUrlReq of(String s3Key) {
        return new PresignedUrlReq(s3Key);
    }

}
