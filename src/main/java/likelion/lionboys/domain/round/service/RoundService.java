package likelion.lionboys.domain.round.service;

import likelion.lionboys.domain.checkin.Checkin;
import likelion.lionboys.domain.checkin.repository.CheckinRepository;
import likelion.lionboys.domain.participant.Participant;
import likelion.lionboys.domain.participant.Role;
import likelion.lionboys.domain.participant.repository.ParticipantRepository;
import likelion.lionboys.domain.party.Party;
import likelion.lionboys.domain.party.repository.PartyRepository;
import likelion.lionboys.domain.round.Round;
import likelion.lionboys.domain.round.RoundStatus;
import likelion.lionboys.domain.round.dto.CreateRoundReq;
import likelion.lionboys.domain.round.dto.CreateRoundResp;
import likelion.lionboys.domain.round.repository.RoundRepository;
import likelion.lionboys.global.exception.CustomException;
import likelion.lionboys.global.exception.error.GlobalErrorCode;
import lombok.RequiredArgsConstructor;
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
    public CreateRoundResp createRound(Long partyId, CreateRoundReq req) {
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
        Participant sec = participantRepo.findByRole(Role.SECRETARY)
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
}
