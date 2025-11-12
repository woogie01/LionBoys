package likelion.lionboys.domain.image.entity;

import jakarta.persistence.*;
import likelion.lionboys.domain.common.BaseTimeEntity;
import likelion.lionboys.domain.image.exception.ImageException;
import likelion.lionboys.domain.image.exception.error.ImageErrorCode;
import likelion.lionboys.domain.party.Party;
import likelion.lionboys.domain.round.Round;

@Table(name = "images",
indexes = @Index(name = "idx_s3_key", columnList = "s3key"))
@Entity
public class Image extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 500)
    private String s3Key;

    @Column(nullable = false)
    private Long fileSize;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UploadStatus uploadStatus;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ContentType contentType;  // DB에 'JPEG', 'PNG' 저장됨

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "round_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_round_party"))
    private Round round;

    /*
    업로드 처리 완료
     */
    public void completeUpload(Long fileSize) {
        if(this.uploadStatus == UploadStatus.PENDING) {
            throw new ImageException(ImageErrorCode.IMAGE_NOT_FOUND, "PENDING 상태에서만 완료 처리할 수 있습니다.");
        }
        this.uploadStatus = UploadStatus.COMPLETED;
        // 등록 시각 변경하기
        this.fileSize = fileSize;
    }

    /*
    업로드 처리 실패
     */
    public void failUpload() {
        this.uploadStatus = UploadStatus.FAILED;
    }

    /*
    업로드 완료된 이미지인지 확인
     */
    public boolean isCompleted() {
        return this.uploadStatus == UploadStatus.COMPLETED;
    }

}
