package likelion.lionboys.domain.party.repository;

import likelion.lionboys.domain.party.Party;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyRepository extends JpaRepository<Party, Long> {

}
