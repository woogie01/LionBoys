package likelion.lionboys.domain.image.repository;

import likelion.lionboys.domain.image.entity.Image;
import likelion.lionboys.domain.image.entity.UploadStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    // S3Key로 조회
    Optional<Image> findByS3Key(String s3Key);

    // 특정 상태이면서 생성일이 특정 시간 이전인 것들만 조회(정리용)
    List<Image> findByUploadStatusAndRegDateBefore(UploadStatus uploadStatus, LocalDateTime regDate);

    // 완료된 이미지만 조회(최신순)
    List<Image> findByUploadStatusOrderByRegDateDesc(UploadStatus status);

    List<Image> findByRound_IdAndUploadStatus(Long roundId, UploadStatus status);
}
