package likelion.lionboys.domain.checkin.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "체크인 응답")
public record CheckinResp(
        Long partyId,
        Long roundId,
        Long participantId
) {}