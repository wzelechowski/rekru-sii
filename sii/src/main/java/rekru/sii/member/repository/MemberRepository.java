package rekru.sii.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rekru.sii.member.model.Member;
import rekru.sii.member.model.MemberStatus;

import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {
    long countByMembershipPlanIdAndStatus(UUID id, MemberStatus status);
}
