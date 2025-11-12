package likelion.lionboys.domain.party.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Party 생성 응답")
public record CreatePartyResp(
        Long partyId,
        String title,
        LocalDateTime eventDate,
        String placeName
) {
}
