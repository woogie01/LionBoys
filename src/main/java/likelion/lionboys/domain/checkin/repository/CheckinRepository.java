package likelion.lionboys.domain.checkin.repository;

import likelion.lionboys.domain.checkin.Checkin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckinRepository extends JpaRepository<Checkin, Long> {

    boolean existsByRoundIdAndParticipantId(Long roundId, Long secId);
}
