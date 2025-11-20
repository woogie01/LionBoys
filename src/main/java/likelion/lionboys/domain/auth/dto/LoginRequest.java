package likelion.lionboys.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Login 요청")
public record LoginRequest(
        @Schema(description = "사용자 이름", example = "김멋사")
        String username,
        @Schema(description = "전화번호", example = "01012345678")
        String phone
) {
}
