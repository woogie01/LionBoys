package likelion.lionboys.domain.image.dto.response;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ImageConfirmResp(
        List<ImageConfirmResult> results
) {
    public record ImageConfirmResult(
            @NotNull
            Long imageId,

            @NotNull
            Boolean success,

            String message
    ) {}

    public static ImageConfirmResp of(List<ImageConfirmResult> results) {
        return new ImageConfirmResp(results);
    }
}