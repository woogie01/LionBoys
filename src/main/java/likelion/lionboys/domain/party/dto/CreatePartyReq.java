package likelion.lionboys.domain.party.dto;

import java.time.LocalDateTime;

public record CreatePartyReq(
        String title,
        LocalDateTime eventDate,
        String placeName,
        String hostName,
        String hostPhone,
        String bankName,
        String accountNumber
) {
}
