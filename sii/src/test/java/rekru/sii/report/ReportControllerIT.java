package rekru.sii.report;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import rekru.sii.gym.model.Gym;
import rekru.sii.gym.repository.GymRepository;
import rekru.sii.member.model.Member;
import rekru.sii.member.model.MemberStatus;
import rekru.sii.member.repository.MemberRepository;
import rekru.sii.membershipPlan.model.MembershipPlan;
import rekru.sii.membershipPlan.model.MembershipPlanType;
import rekru.sii.membershipPlan.repository.MembershipPlanRepository;

import java.math.BigDecimal;
import java.util.UUID;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ReportControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MembershipPlanRepository membershipPlanRepository;

    @Autowired
    private GymRepository gymRepository;

    @BeforeEach
    void cleanUp() {
        memberRepository.deleteAll();
        membershipPlanRepository.deleteAll();
        gymRepository.deleteAll();
    }

    private Gym createGym(String name) {
        Gym gym = Gym.builder()
                .name(name)
                .address("address")
                .phoneNumber("123456789")
                .build();
        return gymRepository.save(gym);
    }

    private MembershipPlan createPlan(Gym gym, String price) {
        MembershipPlan plan = MembershipPlan.builder()
                .name(UUID.randomUUID().toString())
                .type(MembershipPlanType.BASIC)
                .monthlyPrice(new BigDecimal(price))
                .currency("PLN")
                .durationInMonths(12)
                .maxMembers(100)
                .gym(gym)
                .build();
        return membershipPlanRepository.save(plan);
    }

    private void createMember(MembershipPlan plan, MemberStatus status) {
        Member member = Member.builder()
                .fullName(UUID.randomUUID().toString())
                .email(UUID.randomUUID() + "@test.com")
                .status(status)
                .membershipPlan(plan)
                .build();
        memberRepository.save(member);
    }

    @Test
    void shouldGetReportSuccessfully() throws Exception {
        Gym gym1 = createGym("Gym Alpha");
        Gym gym2 = createGym("Gym Beta");

        MembershipPlan plan1 = createPlan(gym1, "120.00");
        MembershipPlan plan2 = createPlan(gym2, "200.00");

        createMember(plan1, MemberStatus.ACTIVE);
        createMember(plan1, MemberStatus.ACTIVE);
        createMember(plan1, MemberStatus.CANCELLED);
        createMember(plan2, MemberStatus.ACTIVE);

        mockMvc.perform(get("/reports")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].gymName").value(hasItems("Gym Alpha", "Gym Beta")))
                .andExpect(jsonPath("$[?(@.gymName == 'Gym Alpha')].amount").value(hasItems(240.00)))
                .andExpect(jsonPath("$[?(@.gymName == 'Gym Alpha')].currency").value(hasItems("PLN")))
                .andExpect(jsonPath("$[?(@.gymName == 'Gym Beta')].amount").value(hasItems(200.00)))
                .andExpect(jsonPath("$[?(@.gymName == 'Gym Beta')].currency").value(hasItems("PLN")));
    }

    @Test
    void shouldGetEmptyReportWhenNoMembersExist() throws Exception {
        mockMvc.perform(get("/reports")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void shouldGetEmptyReportWhenOnlyCancelledMembersExist() throws Exception {
        Gym gym = createGym("Gym Gamma");
        MembershipPlan plan = createPlan(gym, "150.00");
        createMember(plan, MemberStatus.CANCELLED);

        mockMvc.perform(get("/reports")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }
}