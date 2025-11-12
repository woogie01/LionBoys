package likelion.lionboys.domain.party.service;

import likelion.lionboys.domain.checkin.Checkin;
import likelion.lionboys.domain.checkin.repository.CheckinRepository;
import likelion.lionboys.domain.participant.Account;
import likelion.lionboys.domain.participant.Participant;
import likelion.lionboys.domain.participant.Role;
import likelion.lionboys.domain.participant.repository.AccountRepository;
import likelion.lionboys.domain.participant.repository.ParticipantRepository;
import likelion.lionboys.domain.party.Party;
import likelion.lionboys.domain.party.dto.CreatePartyReq;
import likelion.lionboys.domain.party.dto.CreatePartyResp;
import likelion.lionboys.domain.party.repository.PartyRepository;
import likelion.lionboys.domain.round.Round;
import likelion.lionboys.domain.round.RoundStatus;
import likelion.lionboys.domain.round.repository.RoundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PartyService {
    private final PartyRepository partyRepo;
    private final RoundRepository roundRepo;
    private final ParticipantRepository participantRepo;
    private final AccountRepository accountRepo;
    private final CheckinRepository checkinRepo;

    @Transactional
    public CreatePartyResp createPartyWithFirstRound(CreatePartyReq req) {
        // 1. Party 생성
        Party party = Party.builder()
                .title(req.title())
                .eventDate(req.eventDate())
                .build();
        partyRepo.save(party);

        // 2. Round(1차) 생성
        Round round1 = Round.builder()
                .party(party)
                .seqNo(1)
                .placeName(req.placeName())
                .status(RoundStatus.DRAFT)
                .build();
        roundRepo.save(round1);

        // 3. Host(총무) 등록
        Participant host = Participant.builder()
                .party(party)
                .name(req.hostName())
                .phone(req.hostPhone())
                .role(Role.SECRETARY)
                .build();
        participantRepo.save(host);

        // 4. 총무를 1차 자동 체크인
        Checkin checkin = Checkin.builder()
                .round(round1)
                .participant(host)
                .checkedAt(LocalDateTime.now())
                .build();
        checkinRepo.save(checkin);

        // 5. 총무 계좌 등록
        Account account = Account.builder()
                .participant(host)
                .bankName(req.bankName())
                .accountNumber(req.accountNumber())
                .build();
        accountRepo.save(account);

        return new CreatePartyResp(
                party.getId(),
                party.getTitle(),
                party.getEventDate(),
                round1.getPlaceName()
        );
    }
}
