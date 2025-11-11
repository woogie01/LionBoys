package likelion.lionboys.domain.participant.service;

import likelion.lionboys.domain.auth.security.JwtProvider;
import likelion.lionboys.domain.participant.Participant;
import likelion.lionboys.domain.participant.Role;
import likelion.lionboys.domain.participant.dto.SignUpReq;
import likelion.lionboys.domain.participant.dto.SignUpResp;
import likelion.lionboys.domain.participant.repository.ParticipantRepository;
import likelion.lionboys.domain.party.Party;
import likelion.lionboys.domain.party.repository.PartyRepository;
import likelion.lionboys.global.exception.CustomException;
import likelion.lionboys.global.exception.error.GlobalErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ParticipantService {
    private final PartyRepository partyRepo;
    private final ParticipantRepository participantRepo;
    private final JwtProvider jwtProvider;

    @Transactional
    public SignUpResp signUp(Long partyId, SignUpReq req) {
        // 1. 파티 존재 확인
        Party party = partyRepo.findById(partyId)
                .orElseThrow(() -> new CustomException(GlobalErrorCode.NOT_FOUND, "파티를 찾을 수 없습니다."));

        // 2. 참가자 생성
        Participant participant = Participant.builder()
                .party(party)
                .name(req.username())
                .phone(req.phone())
                .role(Role.MEMBER)
                .build();
        participantRepo.save(participant);

        // 3. 토큰 생성
        String accessToken = jwtProvider.createAccessToken(participant.getId(), participant.getRole().name());
        String refreshToken = jwtProvider.createRefreshToken(participant.getId());

        // 4. 응답 반환
        return new SignUpResp(
                participant.getId(),
                req.username(),
                req.phone(),
                accessToken,
                refreshToken
        );
    }


}
