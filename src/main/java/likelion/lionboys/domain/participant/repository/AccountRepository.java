package likelion.lionboys.domain.participant.repository;

import likelion.lionboys.domain.participant.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

}
