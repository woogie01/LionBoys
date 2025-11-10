package likelion.lionboys.domain.party;

import jakarta.persistence.*;
import likelion.lionboys.domain.common.BaseTimeEntity;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@Table(name = "party")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Party extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "party_id")
    private Long id;

    @Column(length = 120, nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDateTime eventDate;

}
