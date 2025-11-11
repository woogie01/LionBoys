package likelion.lionboys.domain.party.exception;

import likelion.lionboys.global.exception.CustomException;
import likelion.lionboys.domain.party.exception.error.PartyErrorCode;

public class PartyException extends CustomException {
    public PartyException(PartyErrorCode code) { super(code); }
    public PartyException(PartyErrorCode code, String message) { super(code, message); }

    public static PartyException duplicateParticipant(Long partyId, String phone) {
        return new PartyException(
                PartyErrorCode.PARTICIPANT_DUPLICATE,
                "파티(" + partyId + ")에 전화번호(" + phone + ") 참여자가 이미 존재합니다."
        );
    }

    public static PartyException roundSeqConflict(Long partyId, int seqNo) {
        return new PartyException(
                PartyErrorCode.ROUND_SEQ_CONFLICT,
                "파티(" + partyId + ")에 차수(" + seqNo + ")가 이미 존재합니다."
        );
    }

    public static PartyException checkinDuplicate(Long roundId, Long participantId) {
        return new PartyException(
                PartyErrorCode.CHECKIN_DUPLICATE,
                "라운드(" + roundId + ")에 참여자(" + participantId + ")가 이미 체크인되었습니다."
        );
    }
}
