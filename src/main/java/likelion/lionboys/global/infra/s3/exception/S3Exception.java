package likelion.lionboys.global.infra.s3.exception;

import com.amazonaws.AmazonServiceException;
import likelion.lionboys.global.exception.CustomException;
import likelion.lionboys.global.infra.s3.exception.error.S3ErrorCode;

public class S3Exception extends CustomException {

    public S3Exception(S3ErrorCode code) {
        super(code);
    }

    public S3Exception(S3ErrorCode code, String message) {
        super(code, message);
    }

    // ========== Static Factory Methods ==========

    /**
     * S3 객체를 찾을 수 없음
     */
    public static S3Exception objectNotFound(String s3Key) {
        return new S3Exception(
                S3ErrorCode.S3_OBJECT_NOT_FOUND,
                String.format("S3 객체를 찾을 수 없습니다: %s", s3Key)
        );
    }

    /**
     * S3 업로드 실패
     */
    public static S3Exception uploadFailed(String s3Key, Throwable cause) {
        return new S3Exception(
                S3ErrorCode.S3_UPLOAD_FAILED,
                String.format("S3 업로드에 실패했습니다: %s - %s", s3Key, cause.getMessage())
        );
    }

    /**
     * S3 다운로드 실패
      */
    public static S3Exception downloadFailed(String s3Key, Throwable cause) {
        return new S3Exception(
                S3ErrorCode.S3_DOWNLOAD_FAILED,
                String.format("S3 다운로드에 실패했습니다: %s - %s", s3Key, cause.getMessage())
        );
    }

    /**
     * S3 삭제 실패
     */
    public static S3Exception deleteFailed(String s3Key, Throwable cause) {
        return new S3Exception(
                S3ErrorCode.S3_DELETE_FAILED,
                String.format("S3 파일 삭제에 실패했습니다: %s - %s", s3Key, cause.getMessage())
        );
    }

    /**
     * 메타데이터 조회 실패
     */
    public static S3Exception metadataRetrievalFailed(String s3Key, Throwable cause) {
        return new S3Exception(
                S3ErrorCode.S3_METADATA_RETRIEVAL_FAILED,
                String.format("S3 메타데이터 조회에 실패했습니다: %s - %s", s3Key, cause.getMessage())
        );
    }

    /**
     * 접근 권한 거부
     */
    public static S3Exception accessDenied(String s3Key) {
        return new S3Exception(
                S3ErrorCode.S3_ACCESS_DENIED,
                String.format("S3 접근 권한이 없습니다: %s", s3Key)
        );
    }

    /**
     * 버킷 접근 권한 거부
     */
    public static S3Exception bucketAccessDenied(String bucketName) {
        return new S3Exception(
                S3ErrorCode.S3_BUCKET_ACCESS_DENIED,
                String.format("버킷 접근 권한이 없습니다: %s", bucketName)
        );
    }

    /**
     * 프레사인드 URL 생성 실패
     */
    public static S3Exception presignedUrlGenerationFailed(String s3Key, Throwable cause) {
        return new S3Exception(
                S3ErrorCode.PRESIGNED_URL_GENERATION_FAILED,
                String.format("프레사인드 URL 생성에 실패했습니다: %s - %s", s3Key, cause.getMessage())
        );
    }

    /**
     * 프레사인드 URL 만료
     */
    public static S3Exception presignedUrlExpired(String url) {
        return new S3Exception(
                S3ErrorCode.PRESIGNED_URL_EXPIRED,
                "프레사인드 URL이 만료되었습니다"
        );
    }

    /**
     * 파일 크기 초과
     */
    public static S3Exception fileSizeExceeded(Long fileSize, Long maxSize) {
        return new S3Exception(
                S3ErrorCode.FILE_SIZE_EXCEEDED,
                String.format("파일 크기가 제한을 초과했습니다: %,d bytes (최대: %,d bytes)",
                        fileSize, maxSize)
        );
    }

    /**
     * 지원하지 않는 Content-Type
     */
    public static S3Exception invalidContentType(String contentType) {
        return new S3Exception(
                S3ErrorCode.INVALID_CONTENT_TYPE,
                String.format("지원하지 않는 파일 형식입니다: %s", contentType)
        );
    }

    public static S3Exception metadataFetchFailed(String s3Key, AmazonServiceException e) {
        return new S3Exception(
                S3ErrorCode.S3_METADATA_RETRIEVAL_FAILED,
                String.format("메타데이터를 찾을 수 없습니다. s3Key: %s", s3Key)
        );
    }

}
