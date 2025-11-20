package likelion.lionboys.domain.auth.service;

import likelion.lionboys.domain.auth.dto.UserDetailsImpl;
import likelion.lionboys.domain.participant.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("authz")
@RequiredArgsConstructor
public class AuthzService {
    private final ParticipantRepository participantRepo;

    public boolean canManageParty(Long partyId, Authentication auth) {
        var u = (UserDetailsImpl) auth.getPrincipal();
        return u != null
                && "SECRETARY".equals(u.role())
                && participantRepo.existsByIdAndPartyId(u.participantId(), partyId);
    }
}