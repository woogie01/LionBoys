package likelion.lionboys.domain.image.exception.error;

import likelion.lionboys.global.exception.error.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ImageErrorCode implements ErrorCode {

    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "IMG404", "이미지가 존재하지 않습니다"),
    IMAGE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "IMG500", "이미지 업로드에 실패했습니다"),
    INVALID_IMAGE_TYPE(HttpStatus.BAD_REQUEST, "IMG400", "지원하지 않는 이미지 형식입니다");

    private final HttpStatus status;
    private final String code;
    private final String message;

    // 명시적으로 작성 (3줄이지만 명확함)
    ImageErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}