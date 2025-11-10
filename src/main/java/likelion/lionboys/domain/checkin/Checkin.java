package likelion.lionboys.domain.checkin;

import jakarta.persistence.*;
import likelion.lionboys.domain.Participant.Participant;
import likelion.lionboys.domain.round.Round;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@Table(
        name = "checkin",
        uniqueConstraints = @UniqueConstraint(name = "uk_checkin_once", columnNames = {"round_id", "participant_id"}),
        indexes = {
                @Index(name = "ix_checkin_round", columnList = "round_id"),
                @Index(name = "ix_checkin_participant", columnList = "participant_id")
        }
)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Checkin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "checkin_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "round_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_checkin_round"))
    private Round round;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_checkin_participant"))
    private Participant participant;

    @Column(nullable = false)
    private LocalDateTime checkedAt = LocalDateTime.now();

}
