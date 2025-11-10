package likelion.lionboys.domain.round;

import jakarta.persistence.*;
import likelion.lionboys.domain.party.Party;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Builder
@Table(
        name = "round",
        uniqueConstraints = @UniqueConstraint(name = "uk_round_seq", columnNames = {"party_id", "seq_no"})
)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Round {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "round_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_round_party"))
    private Party party;

    @Column(name = "seq_no", nullable = false)
    private Integer seqNo;

    @Column(length = 200)
    private String placeName;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(length = 16, nullable = false)
    private RoundStatus status = RoundStatus.DRAFT;

    @Column(precision = 13, scale = 2)
    @Builder.Default
    private BigDecimal totalAmount = BigDecimal.valueOf(0);

}
