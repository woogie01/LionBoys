package likelion.lionboys.domain.round;

public enum RoundStatus {
    DRAFT,        // 진행 중     : 금액 및 명단 수정 가능
    REQUESTED,    // 정산 요청됨 : 잠금 예정
    LOCKED,       // 금액 잠금   : 수정 금지
    CLOSED        // 정산 마감
}
