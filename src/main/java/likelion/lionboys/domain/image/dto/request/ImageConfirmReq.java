package likelion.lionboys.domain.image.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

public record ImageConfirmReq(

        @NotEmpty(message = "확인할 이미지 정보가 필요합니다")
        @Valid
        List<ImageConfirmInfo> images
) {
    public record ImageConfirmInfo(
            @NotNull(message = "imageId는 필수입니다")
            Long imageId,

            @NotBlank(message = "s3Key는 필수입니다")
            String s3Key,

            @NotNull(message = "업로드 결과는 필수입니다")
            Boolean uploadSuccess,

            String errorMessage
    ) {}
}