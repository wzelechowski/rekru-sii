package rekru.sii.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rekru.sii.member.model.Member;
import rekru.sii.report.payload.response.ReportResponse;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReportRepository extends JpaRepository<Member, UUID> {
    @Query("""
            SELECT new rekru.sii.report.payload.response.ReportResponse(
            g.name,
            SUM(mp.monthlyPrice),
            mp.currency
            )
            FROM Member m
            JOIN m.membershipPlan mp
            JOIN mp.gym g
            WHERE m.status = rekru.sii.member.model.MemberStatus.ACTIVE
            GROUP BY g.name, mp.currency
            """)
    List<ReportResponse> getReport();
}
