package likelion.lionboys.domain.round.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "총무 지정 응답")
public record SetSecretaryResp(
        Long partyId,
        Long roundId,
        Long secretaryId
) {
}
