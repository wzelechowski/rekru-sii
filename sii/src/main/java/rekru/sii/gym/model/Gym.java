package rekru.sii.gym.model;

import jakarta.persistence.*;
import lombok.*;
import rekru.sii.membershipPlan.model.MembershipPlan;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "gyms")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Gym {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phoneNumber;

    @OneToMany(mappedBy = "gym", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<MembershipPlan> membershipPlans = new ArrayList<>();

    public void addMembershipPlan(MembershipPlan plan) {
        membershipPlans.add(plan);
        plan.setGym(this);
    }
}
