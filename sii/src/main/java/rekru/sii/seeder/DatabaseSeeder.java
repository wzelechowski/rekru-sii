package rekru.sii.seeder;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import rekru.sii.gym.model.Gym;
import rekru.sii.gym.repository.GymRepository;
import rekru.sii.member.model.Member;
import rekru.sii.member.model.MemberStatus;
import rekru.sii.member.repository.MemberRepository;
import rekru.sii.membershipPlan.model.MembershipPlan;
import rekru.sii.membershipPlan.model.MembershipPlanType;
import rekru.sii.membershipPlan.repository.MembershipPlanRepository;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final GymRepository gymRepository;
    private final MembershipPlanRepository membershipPlanRepository;
    private final MemberRepository memberRepository;

    @Override
    public void run(String... args) throws Exception {
        if (gymRepository.count() > 0) {
            return;
        }

        Gym g1 = Gym.builder()
                .name("FitLife Center")
                .address("Prosta 51, Warszawa")
                .phoneNumber("122333444")
                .build();
        gymRepository.save(g1);

        Gym g2 = Gym.builder()
                .name("Iron Gym")
                .address("Piotrkowska 100, Lodz")
                .phoneNumber("555666777")
                .build();
        gymRepository.save(g2);

        MembershipPlan p1 = MembershipPlan.builder()
                .name("FitLife Basic")
                .type(MembershipPlanType.BASIC)
                .monthlyPrice(new BigDecimal("120.00"))
                .currency("EUR")
                .durationInMonths(12)
                .maxMembers(10)
                .gym(g1)
                .build();
        membershipPlanRepository.save(p1);

        MembershipPlan p2 = MembershipPlan.builder()
                .name("FitLife Premium")
                .type(MembershipPlanType.PREMIUM)
                .monthlyPrice(new BigDecimal("200.00"))
                .currency("EUR")
                .durationInMonths(6)
                .maxMembers(5)
                .gym(g1)
                .build();
        membershipPlanRepository.save(p2);

        MembershipPlan p3 = MembershipPlan.builder()
                .name("Iron Hardcore")
                .type(MembershipPlanType.BASIC)
                .monthlyPrice(new BigDecimal("150.00"))
                .currency("PLN")
                .durationInMonths(1)
                .maxMembers(50)
                .gym(g2)
                .build();
        membershipPlanRepository.save(p3);

        MembershipPlan p4 = MembershipPlan.builder()
                .name("Iron Gym")
                .type(MembershipPlanType.BASIC)
                .monthlyPrice(new BigDecimal("50.00"))
                .currency("USD")
                .durationInMonths(1)
                .maxMembers(50)
                .gym(g2)
                .build();
        membershipPlanRepository.save(p4);

        Member m1 = Member.builder()
                .fullName("Jan Kowalski")
                .email("jan.kowalski@test.com")
                .status(MemberStatus.ACTIVE)
                .membershipPlan(p1)
                .build();
        memberRepository.save(m1);

        Member m2 = Member.builder()
                .fullName("Anna Nowak")
                .email("anna.nowak@test.com")
                .status(MemberStatus.ACTIVE)
                .membershipPlan(p1)
                .build();
        memberRepository.save(m2);

        Member m3 = Member.builder()
                .fullName("Michał Owczarzak")
                .email("owcawk@test.com")
                .status(MemberStatus.CANCELLED)
                .membershipPlan(p2)
                .build();
        memberRepository.save(m3);

        Member m4 = Member.builder()
                .fullName("Mariusz Pudzianowski")
                .email("pudzian@test.com")
                .status(MemberStatus.ACTIVE)
                .membershipPlan(p3)
                .build();
        memberRepository.save(m4);

        Member m5 = Member.builder()
                .fullName("Robert Orzechowski")
                .email("dynamit@test.com")
                .status(MemberStatus.ACTIVE)
                .membershipPlan(p4)
                .build();
        memberRepository.save(m5);
    }
}