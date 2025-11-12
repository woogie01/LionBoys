package likelion.lionboys.domain.participant.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "파티 가입 응답")
public record SignUpResp(
        Long participantId,
        String username,
        String phone,
        String accessToken,
        String refreshToken
) {
}
