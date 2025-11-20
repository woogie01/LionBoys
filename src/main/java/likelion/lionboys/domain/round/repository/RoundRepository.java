package likelion.lionboys.domain.round.repository;

import jakarta.persistence.LockModeType;
import likelion.lionboys.domain.round.Round;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoundRepository extends JpaRepository<Round, Long> {
    boolean existsByPartyIdAndSeqNo(Long partyId, Integer seqNo);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select coalesce(max(r.seqNo), 0) from Round r where r.party.id = :partyId")
    int findMaxSeqNoForUpdate(@Param("partyId") Long partyId);
}
