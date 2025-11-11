package likelion.lionboys.domain.image.dto;


import jakarta.validation.constraints.NotNull;

public record ConfirmUploadReq(
        @NotNull(message = "이미지 ID는 필수입니다")
        Long imageId
) {
}
