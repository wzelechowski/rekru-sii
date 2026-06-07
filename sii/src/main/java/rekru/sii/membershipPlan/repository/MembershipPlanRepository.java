package rekru.sii.membershipPlan.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import rekru.sii.membershipPlan.model.MembershipPlan;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MembershipPlanRepository extends JpaRepository<MembershipPlan, UUID> {
    @EntityGraph(attributePaths = {"gym"})
    Optional<MembershipPlan> getMembershipPlanWithGymById(UUID id);

    List<MembershipPlan> findAllByGymId(UUID gymId);
}
