package likelion.lionboys.domain.participant.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "파티 가입 요청")
public record SignUpReq(
        @Schema(description = "사용자 이름", example = "권현욱")
        String username,
        @Schema(description = "전화번호", example = "01084716933")
        String phone
) {
}
