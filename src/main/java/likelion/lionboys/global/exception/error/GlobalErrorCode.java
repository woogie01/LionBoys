package likelion.lionboys.global.exception.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum GlobalErrorCode implements ErrorCode {

    /* 4xx: 클라이언트 오류 */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "G400", "요청 형식이 올바르지 않습니다. 입력 값을 확인해 주세요."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "G401", "인증이 필요합니다. 유효한 토큰을 포함해 주세요."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "G403", "접근 권한이 없습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "G404", "요청하신 리소스를 찾을 수 없습니다."),
    NOT_SUPPORTED_METHOD(HttpStatus.METHOD_NOT_ALLOWED, "G405", "지원하지 않는 HTTP 메서드입니다."),
    CONFLICT(HttpStatus.CONFLICT, "G409", "요청이 충돌했습니다. 중복 또는 제약조건 위반을 확인해 주세요."),
    UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "G415", "지원하지 않는 Content-Type 입니다. application/json을 사용해 주세요."),

    /* 5xx: 서버 오류 */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "G500", "서버 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    GlobalErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
