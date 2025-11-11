package likelion.lionboys.domain.round.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Round 생성 요청")
public record CreateRoundReq(
        @Schema(description = "모임 장소", example = "메이게츠")
        @NotBlank
        String placeName
) {}