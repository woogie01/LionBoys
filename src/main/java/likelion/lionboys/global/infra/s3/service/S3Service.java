package likelion.lionboys.global.infra.s3.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import likelion.lionboys.domain.image.entity.ContentType;
import likelion.lionboys.global.infra.s3.dto.S3ObjectMetadata;
import likelion.lionboys.global.infra.s3.dto.S3PresignedUrl;
import likelion.lionboys.global.infra.s3.exception.S3Exception;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@RequiredArgsConstructor
@Service
public class S3Service {

    private final AmazonS3 s3Client;

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Value("${aws.s3.presigned-url.upload-expiration-minutes}")
    private Integer expireMinutes;

    /**
     * PUT용 Presigned URL 생성
     *
     * @param s3Key 이미 생성된 S3 객체 키 (e.g., "images/2025/11/15/1/uuid.jpg")
     * @param contentType MIME 타입 (e.g., "image/jpeg")
     * @return Presigned URL 정보
     */
    public S3PresignedUrl generatePutUrl(String s3Key, String contentType) {

        try {
            GeneratePresignedUrlRequest req =
                    new GeneratePresignedUrlRequest(bucket, s3Key)
                    .withMethod(HttpMethod.PUT)
                    .withExpiration(calculateExpirationInDate(expireMinutes))
                    .withContentType(contentType);

            URL url = s3Client.generatePresignedUrl(req);

            return S3PresignedUrl.of(
                    url.toString(),
                    HttpMethod.PUT,
                    expireMinutes * 60,
                    calculateExpirationInDateTime(expireMinutes)
            );

        } catch (AmazonServiceException e) {
            if (e.getStatusCode() == 403) throw S3Exception.accessDenied(s3Key);
            if (e.getStatusCode() == 404) throw S3Exception.bucketAccessDenied(bucket);

            throw S3Exception.uploadFailed(s3Key, e);
        } catch (Exception e) {
            throw S3Exception.presignedUrlGenerationFailed(s3Key, e);
        }
    }

    /**
     * GET용 Presigned URL 생성
     */
    public S3PresignedUrl generateGetUrl(
            String s3Key
    ) {
        try {
            GeneratePresignedUrlRequest req =
                    new GeneratePresignedUrlRequest(bucket, s3Key)
                            .withMethod(HttpMethod.GET)
                            .withExpiration(calculateExpirationInDate(expireMinutes));

            URL url = s3Client.generatePresignedUrl(req);
            return S3PresignedUrl.of(
                    url.toString(),
                    HttpMethod.GET,
                    expireMinutes * 60,
                    calculateExpirationInDateTime(expireMinutes)
            );

        } catch (AmazonServiceException e) {
            if (e.getStatusCode() == 403) throw S3Exception.accessDenied(s3Key);
            if (e.getStatusCode() == 404) throw S3Exception.bucketAccessDenied(bucket);

            throw S3Exception.uploadFailed(s3Key, e);
        } catch (Exception e) {
            throw S3Exception.presignedUrlGenerationFailed(s3Key, e);
        }
    }

    /**
     * S3 객체 메타데이터 조회
     */
    public S3ObjectMetadata getObjectMetadata(String s3Key) {
        try {
            ObjectMetadata metadata = s3Client.getObjectMetadata(bucket, s3Key);

            ContentType contentType = null;
            if (metadata.getContentType() != null) {
                contentType = ContentType.fromMimeType(metadata.getContentType());
            }

            return S3ObjectMetadata.of(
                    metadata.getContentLength(),
                    contentType,
                    metadata.getLastModified()
            );

        } catch (AmazonServiceException e) {
            if (e.getStatusCode() == 404) {
                throw S3Exception.objectNotFound(s3Key);
            }
            throw S3Exception.metadataFetchFailed(s3Key, e);
        }
    }

    /**
     * S3 객체 존재 여부 확인
     */
    public boolean objectExists(String s3Key) {
        try {
            s3Client.getObjectMetadata(bucket, s3Key);
            return true;
        } catch (AmazonServiceException e) {
            if (e.getStatusCode() == 404) {
                return false;
            }
            throw S3Exception.metadataFetchFailed(s3Key, e);
        }
    }

    // ================= Helper ================

    /**
     * AWS SDK용 만료 시간 (Date 타입)
     */
    private Date calculateExpirationInDate(int minutes) {
        return Date.from(Instant.now().plus(minutes, ChronoUnit.MINUTES));
    }

    /**
     * 우리 DTO용 만료 시간 (LocalDateTime 타입)
     */
    private LocalDateTime calculateExpirationInDateTime(int minutes) {
        return LocalDateTime.from(Instant.now().plus(minutes, ChronoUnit.MINUTES));
    }
}
