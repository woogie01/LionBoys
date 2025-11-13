package likelion.lionboys.domain.participant.repository;

import likelion.lionboys.domain.participant.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    boolean existsByIdAndPartyId(Long participantId, Long partyId);
    Optional<Participant> findByIdAndPartyId(Long participantId, Long partyId);
}
