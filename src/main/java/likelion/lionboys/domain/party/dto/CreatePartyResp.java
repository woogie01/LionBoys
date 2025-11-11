package likelion.lionboys.domain.party.dto;

import java.time.LocalDateTime;

public record CreatePartyResp(
        String title,
        LocalDateTime eventDate,
        String placeName
) {
}
