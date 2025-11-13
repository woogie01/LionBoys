package likelion.lionboys.domain.image.dto.request;

import jakarta.validation.constraints.NotNull;
import likelion.lionboys.domain.image.entity.UploadStatus;

import java.time.LocalDateTime;

public record ImageGetReq(
        @NotNull(message = "roundId는 필수")
        Long roundId,

        // 선택적 필터링 옵션들
        UploadStatus status,  // null이면 COMPLETED만 조회

        LocalDateTime uploadedAfter  // 특정 시점 이후 업로드된 것만
) {}