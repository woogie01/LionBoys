package likelion.lionboys.global.infra.s3.exception;

import likelion.lionboys.global.exception.error.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum S3ErrorCode implements ErrorCode {

    // S3 연결/설정 에러
    S3_CONNECTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S3_500_001", "S3 연결에 실패했습니다"),
    S3_CONFIGURATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S3_500_002", "S3 설정이 올바르지 않습니다"),

    // 객체 관련 에러
    S3_OBJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "S3_404_001", "S3에서 파일을 찾을 수 없습니다"),
    S3_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S3_500_003", "S3 업로드에 실패했습니다"),
    S3_DOWNLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S3_500_004", "S3 다운로드에 실패했습니다"),
    S3_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S3_500_005", "S3 파일 삭제에 실패했습니다"),

    // 메타데이터 에러
    S3_METADATA_RETRIEVAL_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S3_500_006", "S3 메타데이터 조회에 실패했습니다"),

    // 권한 에러
    S3_ACCESS_DENIED(HttpStatus.FORBIDDEN, "S3_403_001", "S3 접근 권한이 없습니다"),
    S3_BUCKET_ACCESS_DENIED(HttpStatus.FORBIDDEN, "S3_403_002", "버킷 접근 권한이 없습니다"),

    // 프레사인드 URL 에러
    PRESIGNED_URL_GENERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S3_500_007", "프레사인드 URL 생성에 실패했습니다"),
    PRESIGNED_URL_EXPIRED(HttpStatus.BAD_REQUEST, "S3_400_001", "프레사인드 URL이 만료되었습니다"),

    // 파일 검증 에러
    INVALID_FILE_SIZE(HttpStatus.BAD_REQUEST, "S3_400_002", "파일 크기가 유효하지 않습니다"),
    FILE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "S3_400_003", "파일 크기가 제한을 초과했습니다"),
    INVALID_CONTENT_TYPE(HttpStatus.BAD_REQUEST, "S3_400_004", "지원하지 않는 파일 형식입니다");

    private final HttpStatus status;
    private final String code;
    private final String message;

    // 명시적으로 작성 (3줄이지만 명확함)
    S3ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
