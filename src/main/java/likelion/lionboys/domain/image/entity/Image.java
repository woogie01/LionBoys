package likelion.lionboys.domain.image.entity;

import jakarta.persistence.*;
import likelion.lionboys.domain.common.BaseTimeEntity;
import likelion.lionboys.domain.image.exception.ImageException;
import likelion.lionboys.domain.image.exception.error.ImageErrorCode;
import likelion.lionboys.domain.round.Round;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Table(name = "images",
        indexes = {
                @Index(name = "idx_s3_key", columnList = "s3Key"),
                @Index(name = "idx_round_status", columnList = "round_id, uploadStatus"),
                @Index(name = "idx_created_at", columnList = "createdAt")
        }
)
@Entity
public class Image extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 500)
    private String s3Key;

    @Column(length = 255)
    private String originalFileName;  // 원본 파일명

    @Column(nullable = false)
    private Long fileSize;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UploadStatus uploadStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ContentType contentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "round_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_image_round"))
    private Round round;

    // ==================== 비즈니스 로직 ====================

    /**
     * 업로드 처리 완료
     */
    public void completeUpload(Long fileSize) {
        if (this.uploadStatus != UploadStatus.PENDING) {
            throw new ImageException(
                    ImageErrorCode.INVALID_STATUS,
                    "PENDING 상태에서만 완료 처리할 수 있습니다. 현재 상태: " + this.uploadStatus
            );
        }
        this.uploadStatus = UploadStatus.COMPLETED;
        this.fileSize = fileSize;
    }

    /**
     * 업로드 처리 실패
     */
    public void failUpload() {
        this.uploadStatus = UploadStatus.FAILED;
    }

    /**
     * 업로드 완료된 이미지인지 확인
     */
    public boolean isCompleted() {
        return this.uploadStatus == UploadStatus.COMPLETED;
    }

    // ==================== 정적 팩토리 메서드 ====================

    /**
     * PENDING 상태의 이미지 생성 (Presigned URL 발급 시)
     */
    public static Image createPending(
            String s3Key,
            String originalFileName,
            Long fileSize,
            ContentType contentType,
            Round round
    ) {
        return Image.builder()
                .s3Key(s3Key)
                .originalFileName(originalFileName)
                .fileSize(fileSize)
                .contentType(contentType)
                .uploadStatus(UploadStatus.PENDING)
                .round(round)
                .build();
    }
}