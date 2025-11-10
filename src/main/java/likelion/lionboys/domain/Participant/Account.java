package likelion.lionboys.domain.Participant;

import jakarta.persistence.*;
import likelion.lionboys.domain.common.BaseTimeEntity;
import lombok.*;

@Entity
@Getter
@Builder
@Table(
        name = "account",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_acct_global", columnNames = {"bank_name", "account_number"})
        }
)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id", nullable = false, unique = true,
            foreignKey = @ForeignKey(name = "fk_account_participant"))
    private Participant participant;

    @Column(name = "bank_name", length = 40, nullable = false)
    private String bankName;

    @Column(name = "account_number", length = 64, nullable = false)
    private String accountNumber;

}
