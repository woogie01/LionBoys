package likelion.lionboys.domain.party.exception.error;

import likelion.lionboys.global.exception.error.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum PartyErrorCode implements ErrorCode {

    /* 409: 충돌/중복(제약조건 위반) */
    PARTICIPANT_DUPLICATE(HttpStatus.CONFLICT, "PT409", "해당 파티에 동일 전화번호의 참여자가 이미 존재합니다."),
    ROUND_SEQ_CONFLICT(HttpStatus.CONFLICT, "R409", "해당 파티에 이미 동일한 차수 번호가 존재합니다."),
    CHECKIN_DUPLICATE(HttpStatus.CONFLICT, "CK409", "해당 라운드에 이미 체크인된 참여자입니다."),
    ALLOCATION_ALREADY_EXISTS(HttpStatus.CONFLICT, "AL409", "해당 체크인의 배분 정보가 이미 존재합니다.")
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;

    PartyErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
