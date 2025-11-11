package likelion.lionboys.domain.image.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UploadStatus {
    PENDING,    // 업로드 대기
    COMPLETED,  // 업로드 완료
    FAILED,     // 업로드 실패
    DELETED     // 삭제됨
}
