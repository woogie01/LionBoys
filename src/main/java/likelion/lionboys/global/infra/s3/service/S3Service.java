package likelion.lionboys.global.infra.s3.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
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
    private Integer uploadExpireMinutes;

    @Value("${aws.s3.presigned-url.download-expiration-days}")
    private Integer downloadExpireDays;

    //PUT용 Presigned URL 생성(업로드용)
    public PresignedUrlResp generatePutUrl(PresignedUrlReq req) {

        Date expiration = calculateExpiration(uploadExpireMinutes);
        String s3Key = buildS3Key(req.roundId());

        try {

            GeneratePresignedUrlRequest presignedUrlReq =
                    new GeneratePresignedUrlRequest(bucket, s3Key)
                            .withMethod(HttpMethod.PUT)
                            .withExpiration(expiration)
                            .withContentType(req.contentType());

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
    private String buildS3Key(Long roundId) {

        LocalDateTime now = LocalDateTime.now();
        String uuid = UUID.randomUUID().toString();

        return String.format("images/%d/%02d/%02d/%d/%s",
                now.getYear(),
                now.getMonthValue(),
                now.getDayOfMonth(),
                roundId,
                uuid
        );
    }
}
