package likelion.lionboys.domain.party.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Party 생성 요청")
public record CreatePartyReq(
        @Schema(description = "모임명", example = "장프 뒷풀이")
        String title,
        @Schema(description = "모임 날짜", example = "")
        LocalDateTime eventDate,
        @Schema(description = "모임 장소", example = "거기꼬치네")
        String placeName,
        @Schema(description = "총무 이름", example = "김멋사")
        String hostName,
        @Schema(description = "총무 전화번호", example = "01012345678")
        String hostPhone,
        @Schema(description = "은행명", example = "하나은행")
        String bankName,
        @Schema(description = "계좌번호", example = "24589069517507")
        String accountNumber
) {
}
