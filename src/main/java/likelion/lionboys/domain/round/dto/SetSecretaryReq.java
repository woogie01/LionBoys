package likelion.lionboys.domain.round.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "총무 지정 요청")
public record SetSecretaryReq(
        @NotNull
        Long participantId
) {
}
