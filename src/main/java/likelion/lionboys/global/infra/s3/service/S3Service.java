package likelion.lionboys.global.infra.s3.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import likelion.lionboys.domain.image.entity.ContentType;
import likelion.lionboys.global.infra.s3.dto.PresignedUrlReq;
import likelion.lionboys.global.infra.s3.dto.PresignedUrlResp;
import likelion.lionboys.global.infra.s3.exception.S3Exception;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class S3Service {

    private final AmazonS3 s3Client;

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Value("${aws.s3.presigned-url.upload-expiration-minutes}")
    private Integer expireMinutes;


    //PUT용 Presigned URL 생성(업로드용)
    public PresignedUrlResp generatePutUrl(PresignedUrlReq req) {

        Date expiration = calculateExpiration(expireMinutes);
        String s3Key = buildS3Key(req.roundId(), req.contentType());
        String extension = getExtensionFromContentType(req.contentType());

        try {

            GeneratePresignedUrlRequest presignedUrlReq =
                    new GeneratePresignedUrlRequest(bucket, s3Key)
                            .withMethod(HttpMethod.PUT)
                            .withExpiration(expiration)
                            .withContentType(extension);

            URL url = s3Client.generatePresignedUrl(presignedUrlReq);

            return PresignedUrlResp.of(
                    url.toString(),
                    null,
                    HttpMethod.PUT.toString(),
                    s3Key
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
     * GET용 Presigned URL 생성 (다운로드용)
     * ImageService로부터 s3Key를 받아 S3로부터 임시 URL을 생성합니다.
     *
     * @param s3Key DB에 저장된 실제 S3 객체 키 (e.g., "images/.../uuid.jpg")
     * @return PresignedUrlResp
     */
    public PresignedUrlResp generateGetUrl(String s3Key) {

        // 비즈니스 로직(1일 만료)은 ImageService에서 처리합니다.
        // S3Service는 S3가 정한 짧은 만료 시간(e.g., 10분)만 설정합니다.
        Date expiration = calculateExpiration(expireMinutes); // PUT과 동일하게 짧은 만료 시간 사용

        try {
            GeneratePresignedUrlRequest presignedUrlReq =
                    new GeneratePresignedUrlRequest(bucket, s3Key)
                            .withMethod(HttpMethod.GET)
                            .withExpiration(expiration); // 짧은 만료 시간

            URL url = s3Client.generatePresignedUrl(presignedUrlReq);

            return PresignedUrlResp.of(
                    url.toString(),
                    null, // GET은 contentType 응답이 필요 없음
                    HttpMethod.GET.toString(),
                    s3Key
            );
        } catch (AmazonServiceException e) {
            // GET 요청 시 404는 "버킷 접근 거부"가 아니라 "객체를 찾을 수 없음"입니다.
            if (e.getStatusCode() == 403) throw S3Exception.accessDenied(s3Key);
            if (e.getStatusCode() == 404) throw S3Exception.objectNotFound(s3Key);

            throw S3Exception.downloadFailed(s3Key, e); // S3 관련 다운로드 실패

        } catch (Exception e) {
            // S3 외의 일반 오류 (e.g., NullPointerException)
            throw S3Exception.presignedUrlGenerationFailed(s3Key, e);
        }
    }


    // ================= Helper ================

    /**
     * 만료 시간 계산 (일 단위)
     */
    private Date calculateExpiration(int minutes) {
        return Date.from(Instant.now().plus(minutes, ChronoUnit.MINUTES));
    }

    /**
     * 만료 시간 계산 (일 단위)
     */
    private Date calculateExpirationInDays(int days) {
        return Date.from(Instant.now().plus(days, ChronoUnit.DAYS));
    }

    // UUID를 활용하여 S3 고유키를 만들어주는 함수
    /**
     * S3 고유 키 생성
     * 형식: images/2025/11/15/roundId/uuid.jpg
     */
    private String buildS3Key(Long roundId, String extension) {

        LocalDateTime now = LocalDateTime.now();
        String uuid = UUID.randomUUID().toString();

        return String.format("images/%d/%02d/%02d/%d/%s.%s",
                now.getYear(),
                now.getMonthValue(),
                now.getDayOfMonth(),
                roundId,
                uuid,
                extension
        );
    }

    /**
     * Content-Type(MIME Type)에서 파일 확장자를 추출
     * (Image 엔티티의 ContentType Enum과 연동되도록 구현 필요)
     */
    private String getExtensionFromContentType(String contentType) {
        if (contentType == null) {
            throw S3Exception.invalidContentType(contentType);
        }

        return ContentType.fromMimeType(contentType).toString();
    }
}
