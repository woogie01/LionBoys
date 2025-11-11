package likelion.lionboys.domain.participant;

import jakarta.persistence.*;
import likelion.lionboys.domain.common.BaseTimeEntity;
import likelion.lionboys.domain.party.Party;
import lombok.*;

@Entity
@Getter
@Builder
@Table(
        name = "participant",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_party_phone", columnNames = {"party_id", "phone"})
        }
)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Participant extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "participant_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_participant_party"))
    private Party party;

    @Column(length = 60, nullable = false)
    private String name;

    @Column(length = 30, nullable = false)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(length = 16, nullable = false)
    @Builder.Default
    private Role role = Role.MEMBER;

}
