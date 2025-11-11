package likelion.lionboys.domain.round.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import likelion.lionboys.domain.round.RoundStatus;

@Schema(description = "Round 생성 응답")
public record CreateRoundResp(
        Long partyId,
        Long roundId,
        Integer seqNo,
        String placeName,
        RoundStatus status
) { }