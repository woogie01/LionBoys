package likelion.lionboys.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Login 응답")
public record LoginResponse(
        Long participantId,
        String username,
        String accessToken,
        String refreshToken
) {
}