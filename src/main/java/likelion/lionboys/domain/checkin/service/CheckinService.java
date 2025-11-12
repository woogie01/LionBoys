package likelion.lionboys.domain.checkin.service;

import likelion.lionboys.domain.checkin.Checkin;
import likelion.lionboys.domain.checkin.dto.CheckinResp;
import likelion.lionboys.domain.checkin.repository.CheckinRepository;
import likelion.lionboys.domain.participant.Participant;
import likelion.lionboys.domain.participant.repository.ParticipantRepository;
import likelion.lionboys.domain.party.exception.PartyException;
import likelion.lionboys.domain.party.exception.error.PartyErrorCode;
import likelion.lionboys.domain.round.Round;
import likelion.lionboys.domain.round.repository.RoundRepository;
import likelion.lionboys.global.exception.CustomException;
import likelion.lionboys.global.exception.error.GlobalErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CheckinService {
    private final RoundRepository roundRepo;
    private final ParticipantRepository participantRepo;
    private final CheckinRepository checkinRepo;

    @Transactional
    public CheckinResp checkin(Long partyId, Long roundId, Long myId) {
        if (myId == null) {
            throw new CustomException(GlobalErrorCode.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        // 1.라운드 및 파티 검증
        Round round = roundRepo.findById(roundId)
                .orElseThrow(() -> new CustomException(GlobalErrorCode.NOT_FOUND, "라운드를 찾을 수 없습니다."));
        if (!round.getParty().getId().equals(partyId)) {
            throw new CustomException(GlobalErrorCode.BAD_REQUEST, "라운드가 해당 파티에 속하지 않습니다.");
        }

        // 2 참가자 검증
        Participant participant = participantRepo.findByIdAndPartyId(myId, partyId)
                .orElseThrow(() -> new CustomException(GlobalErrorCode.NOT_FOUND, "해당 파티의 참가자가 아닙니다."));

        // 3. 중복 체크인 방지
        if (checkinRepo.existsByRoundIdAndParticipantId(roundId, myId)) {
            throw new PartyException(PartyErrorCode.CHECKIN_DUPLICATE, "이미 체크인했습니다.");
        }

        // 4. 체크인 생성
        LocalDateTime now = LocalDateTime.now();
        Checkin ci = Checkin.builder()
                .round(round)
                .participant(participant)
                .checkedAt(now)
                .build();
        checkinRepo.save(ci);

        return new CheckinResp(
                partyId,
                roundId,
                participant.getId()
        );
    }
}
