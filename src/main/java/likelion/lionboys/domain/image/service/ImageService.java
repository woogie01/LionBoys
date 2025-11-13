package likelion.lionboys.domain.image.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import likelion.lionboys.domain.image.dto.request.ImageConfirmReq;
import likelion.lionboys.domain.image.dto.request.ImageGetReq;
import likelion.lionboys.domain.image.dto.request.PresignedUrlReq;
import likelion.lionboys.domain.image.dto.response.ImageConfirmResp;
import likelion.lionboys.domain.image.dto.response.ImageGetResp;
import likelion.lionboys.domain.image.dto.response.PresignedUrlResp;
import likelion.lionboys.domain.image.entity.ContentType;
import likelion.lionboys.domain.image.entity.Image;
import likelion.lionboys.domain.image.entity.UploadStatus;
import likelion.lionboys.domain.image.exception.ImageException;
import likelion.lionboys.domain.image.exception.error.ImageErrorCode;
import likelion.lionboys.domain.image.repository.ImageRepository;
import likelion.lionboys.domain.round.Round;
import likelion.lionboys.domain.round.repository.RoundRepository;
import likelion.lionboys.global.infra.s3.dto.S3ObjectMetadata;
import likelion.lionboys.global.infra.s3.dto.S3PresignedUrl;
import likelion.lionboys.global.infra.s3.exception.S3Exception;
import likelion.lionboys.global.infra.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final RoundRepository roundRepository;
    private final S3Service s3Service;

    /**
     * Presigned URL 발급
     */
    @Transactional
    public PresignedUrlResp getPutUrls(PresignedUrlReq req) {

        Round round = roundRepository.findById(req.roundId())
                .orElseThrow(() -> new EntityNotFoundException("Round not found with id: " + req.roundId()));
        // 이후 RoundException으로 교체할 예정

        List<PresignedUrlResp.PresignedUrlInfo> urlInfos = req.images().stream()
                .map(uploadInfo -> {

                    String s3Key = buildS3Key(
                            round.getId(),
                            uploadInfo.contentType()
                    );

                    Image pendingImage = Image.createPending(
                            s3Key,
                            uploadInfo.originalFileName(),
                            uploadInfo.fileSize(),
                            ContentType.fromMimeType(uploadInfo.contentType()),
                            round
                    );

                    Image savedImage = imageRepository.save(pendingImage);

                    S3PresignedUrl s3Url = s3Service.generatePutUrl(
                            s3Key,
                            uploadInfo.contentType()
                    );

                    log.info("Presigned URL 발급 완료: imageId={}, s3Key={}, originalFileName={}",
                            savedImage.getId(), s3Key, uploadInfo.originalFileName());

                    return new PresignedUrlResp.PresignedUrlInfo(
                            savedImage.getId(),
                            s3Url.url(),
                            s3Key,
                            s3Url.expiresInSeconds(),
                            s3Url.expiredAt(),
                            uploadInfo.originalFileName()
                    );
                })
                .toList();

        return PresignedUrlResp.of(urlInfos);
    }

    @Transactional
    public ImageConfirmResp confirm(ImageConfirmReq req) {

        Round round = roundRepository.findById(req.roundId())
                .orElseThrow(() -> new EntityNotFoundException("Round not found with id: " + req.roundId()));
        // 이후 RoundException으로 교체할 예정

        List<ImageConfirmResp.ImageConfirmResult> results = req.images().stream()
                .map(confirmInfo -> processConfirm(confirmInfo))
                .toList();

        return ImageConfirmResp.of(results);
    }

    /**
     * 다운로드 URL 조회
     */
    @Transactional
    public ImageGetResp getDownloadUrls(ImageGetReq req) {

        // 1. COMPLETED 상태의 이미지 조회
        List<Image> images = imageRepository.findByRound_IdAndUploadStatus(
                req.roundId(),
                UploadStatus.COMPLETED
        );

        if (images.isEmpty()) {
            throw new ImageException(
                    ImageErrorCode.IMAGE_NOT_FOUND,
                    "Round " + req.roundId() + "에 업로드된 이미지가 없습니다"
            );
        }

        List<ImageGetResp.ImageInfo> imageInfos = images.stream()
                .map(image -> {
                    S3PresignedUrl s3Url = s3Service.generateGetUrl(
                            image.getS3Key()
                    );

                    return new ImageGetResp.ImageInfo(
                            image.getId(),
                            s3Url.url(),
                            image.getS3Key(),
                            image.getOriginalFileName(),
                            image.getFileSize(),
                            image.getContentType(),
                            image.getRegDate(),
                            s3Url.expiresInSeconds()
                    );
                })
                .toList();

        log.info("다운로드 URL 생성 완료: roundId={}, imageCount={}",
                req.roundId(), imageInfos.size());

        return ImageGetResp.of(req.roundId(), imageInfos);
    }
    // ==================== Helper ==============================

    /**
     * 개별 이미지 Confirm 처리
     */
    private ImageConfirmResp.ImageConfirmResult processConfirm(
            ImageConfirmReq.ImageConfirmInfo confirmInfo
    ) {
        // 1. Image 엔티티 조회
        Image image = imageRepository.findById(confirmInfo.imageId())
                .orElseThrow(() -> new ImageException(
                        ImageErrorCode.IMAGE_NOT_FOUND,
                        "Image not found with id: " + confirmInfo.imageId()
                ));

        return confirmInfo.uploadSuccess() ?
                handleSuccessfulUpload(image, confirmInfo) :
                handleFailedUpload(image, confirmInfo);
    }

    private ImageConfirmResp.ImageConfirmResult handleSuccessfulUpload(
            Image image,
            ImageConfirmReq.ImageConfirmInfo confirmInfo
    ) {
        // S3 메타데이터 조회
        S3ObjectMetadata metadata = s3Service.getObjectMetadata(image.getS3Key());

        // COMPLETED로 변경
        image.completeUpload(metadata.contentLength());

        log.info("이미지 업로드 완료: imageId={}, s3Key={}, fileSize={}",
                image.getId(), image.getS3Key(), metadata.contentLength());

        return new ImageConfirmResp.ImageConfirmResult(
                confirmInfo.imageId(),
                true,
                "업로드 완료"
        );
    }

    private ImageConfirmResp.ImageConfirmResult handleFailedUpload(
            Image image,
            ImageConfirmReq.ImageConfirmInfo confirmInfo
    ) {
        // FAILED로 변경
        image.failUpload();

        log.error("이미지 업로드 실패: imageId={}, error={}",
                image.getId(), confirmInfo.errorMessage());

        return new ImageConfirmResp.ImageConfirmResult(
                confirmInfo.imageId(),
                false,
                confirmInfo.errorMessage() != null ?
                        confirmInfo.errorMessage() : "업로드 실패"
        );
    }

    /**
     * S3 고유 키 생성
     * 형식: images/roundId/uuid.jpg
     */
    private String buildS3Key(Long roundId, String extension) {

        String uuid = UUID.randomUUID().toString();

        return String.format("images/%d/%s.%s",
                roundId,
                uuid,
                extension
        );
    }

    /**
     * Content-Type(MIME Type)에서 파일 확장자를 추출
     */
    private String getExtensionFromContentType(String contentType) {
        if (contentType == null) {
            throw S3Exception.invalidContentType(contentType);
        }

        return ContentType.fromMimeType(contentType).toString();
    }
}
