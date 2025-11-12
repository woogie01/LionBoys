package likelion.lionboys.domain.round.service;

import likelion.lionboys.domain.checkin.Checkin;
import likelion.lionboys.domain.checkin.repository.CheckinRepository;
import likelion.lionboys.domain.participant.Participant;
import likelion.lionboys.domain.participant.repository.ParticipantRepository;
import likelion.lionboys.domain.party.Party;
import likelion.lionboys.domain.party.repository.PartyRepository;
import likelion.lionboys.domain.round.Round;
import likelion.lionboys.domain.round.RoundStatus;
import likelion.lionboys.domain.round.dto.CreateRoundReq;
import likelion.lionboys.domain.round.dto.CreateRoundResp;
import likelion.lionboys.domain.round.dto.SetSecretaryReq;
import likelion.lionboys.domain.round.dto.SetSecretaryResp;
import likelion.lionboys.domain.round.repository.RoundRepository;
import likelion.lionboys.global.exception.CustomException;
import likelion.lionboys.global.exception.error.GlobalErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RoundService {
    private final PartyRepository partyRepo;
    private final RoundRepository roundRepo;
    private final ParticipantRepository participantRepo;
    private final CheckinRepository checkinRepo;

    @Transactional
    public CreateRoundResp createRound(Long partyId, CreateRoundReq req, Long secretaryId) {
        // 1. 파티 존재 확인
        Party party = partyRepo.findById(partyId)
                .orElseThrow(() -> new CustomException(GlobalErrorCode.NOT_FOUND, "파티를 찾을 수 없습니다."));

        // 2. 차수 결정
        int max = roundRepo.findMaxSeqNoForUpdate(party.getId());
        int seqNo = max + 1;

        // 3. Round 생성
        Round round = Round.builder()
                .party(party)
                .seqNo(seqNo)
                .placeName(req.placeName())
                .status(RoundStatus.DRAFT)
                .build();
        roundRepo.save(round);

        // 4. 총무 체크인
        Participant sec = participantRepo.findById(secretaryId)
                .orElseThrow(() -> new CustomException(GlobalErrorCode.NOT_FOUND, "총무를 찾을 수 없습니다."));
        if (!checkinRepo.existsByRoundIdAndParticipantId(round.getId(), sec.getId())) {
            var checkin = Checkin.builder()
                    .round(round)
                    .participant(sec)
                    .checkedAt(LocalDateTime.now())
                    .build();
            checkinRepo.save(checkin);
        }

        return new CreateRoundResp(
                party.getId(),
                round.getId(),
                round.getSeqNo(),
                round.getPlaceName(),
                round.getStatus()
        );
    }

    @Transactional
    @PreAuthorize("@authz.canManageParty(#partyId, authentication)")
    public SetSecretaryResp setSecretary(Long partyId, Long roundId, SetSecretaryReq req) {
        // 1. 파티 및 차수 검증
        Round round = roundRepo.findById(roundId)
                .orElseThrow(() -> new CustomException(GlobalErrorCode.NOT_FOUND, "라운드를 찾을 수 없습니다."));
        if (!round.getParty().getId().equals(partyId)) {
            throw new CustomException(GlobalErrorCode.BAD_REQUEST, "라운드가 해당 파티에 속하지 않습니다.");
        }

        // 2. 참가자 검증
        Participant participant = participantRepo.findByIdAndPartyId(req.participantId(), partyId)
                .orElseThrow(() -> new CustomException(GlobalErrorCode.NOT_FOUND, "같은 파티의 참가자가 아닙니다."));

        // 3. 총무 지정
        round.registerSecretary(participant);

        // 4. 총무 자동 체크인
        if (!checkinRepo.existsByRoundIdAndParticipantId(roundId, participant.getId())) {
            var ci = Checkin.builder()
                    .round(round)
                    .participant(participant)
                    .checkedAt(LocalDateTime.now())
                    .build();
            checkinRepo.save(ci);
        }

        return new SetSecretaryResp(partyId, roundId, participant.getId());
    }

}
