package likelion.lionboys.domain.allocation;

import jakarta.persistence.*;
import likelion.lionboys.domain.checkin.Checkin;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Builder
@Table(
        name = "allocation",
        uniqueConstraints = @UniqueConstraint(name = "uk_alloc_once", columnNames = {"checkin_id"})
)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Allocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "allocation_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checkin_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_alloc_checkin"))
    private Checkin checkin;

    @Column(precision = 13, scale = 2, nullable = false)
    private BigDecimal amount;
}
