package likelion.lionboys.domain.image.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UploadImageReq(
    @NotBlank(message = "Content-Type은 필수입니다")
    String contentType
) {

}
