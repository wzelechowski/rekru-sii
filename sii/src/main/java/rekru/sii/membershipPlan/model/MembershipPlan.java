package rekru.sii.membershipPlan.model;

import jakarta.persistence.*;
import lombok.*;
import rekru.sii.gym.model.Gym;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "membership_plans")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MembershipPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MembershipPlanType type;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monthlyPrice;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private Integer durationInMonths;

    @Column(nullable = false)
    private Integer maxMembers;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gym_id", nullable = false)
    private Gym gym;
}
