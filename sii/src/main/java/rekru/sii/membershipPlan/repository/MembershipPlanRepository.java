package rekru.sii.membershipPlan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rekru.sii.membershipPlan.model.MembershipPlan;

import java.util.UUID;

public interface MembershipPlanRepository extends JpaRepository<MembershipPlan, UUID> {
}
