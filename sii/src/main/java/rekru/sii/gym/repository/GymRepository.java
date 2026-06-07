package rekru.sii.gym.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rekru.sii.gym.model.Gym;
import rekru.sii.gym.payload.response.ReportResponse;

import java.util.List;
import java.util.UUID;

public interface GymRepository extends JpaRepository<Gym, UUID> {
    @Query("""
            SELECT new rekru.sii.gym.payload.response.ReportResponse(
            g.name,
            SUM(mp.monthlyPrice),
            mp.currency
            )
            FROM Gym g
            JOIN g.membershipPlans mp
            JOIN mp.members m
            WHERE m.status = rekru.sii.member.model.MemberStatus.ACTIVE
            GROUP BY g.name, mp.currency
            """)
    List<ReportResponse> getReport();
}
