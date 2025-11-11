package likelion.lionboys.domain.participant.repository;

import likelion.lionboys.domain.participant.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

}
