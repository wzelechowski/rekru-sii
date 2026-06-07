package rekru.sii.member.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import rekru.sii.member.model.Member;
import rekru.sii.member.model.MemberStatus;

import java.util.List;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {
    long countByMembershipPlanIdAndStatus(UUID id, MemberStatus status);

    @Override
    @EntityGraph(attributePaths = {"membershipPlan", "membershipPlan.gym"})
    List<Member> findAll();
}
