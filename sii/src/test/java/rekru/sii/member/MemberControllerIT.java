package rekru.sii.member;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import rekru.sii.member.payload.request.MemberRequest;
import rekru.sii.member.repository.MemberRepository;
import rekru.sii.membershipPlan.model.MembershipPlan;
import rekru.sii.membershipPlan.model.MembershipPlanType;
import rekru.sii.membershipPlan.repository.MembershipPlanRepository;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MemberControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MembershipPlanRepository membershipPlanRepository;

    @Autowired
    private GymRepository gymRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MembershipPlan createPlan(int maxMembers) {
        Gym gym = Gym.builder()
                .name(UUID.randomUUID().toString())
                .address("address")
                .phoneNumber("123456789")
                .build();
        gymRepository.save(gym);

        MembershipPlan plan = MembershipPlan.builder()
                .name(UUID.randomUUID().toString())
                .type(MembershipPlanType.BASIC)
                .monthlyPrice(new BigDecimal("100.00"))
                .currency("PLN")
                .durationInMonths(6)
                .maxMembers(maxMembers)
                .gym(gym)
                .build();
        return membershipPlanRepository.save(plan);
    }

    private Member createMember(MembershipPlan plan) {
        return createMember(plan, MemberStatus.ACTIVE);
    }

    private Member createMember(MembershipPlan plan, MemberStatus status) {
        Member member = Member.builder()
                .fullName(UUID.randomUUID().toString())
                .email(UUID.randomUUID() + "@test.com")
                .status(status)
                .membershipPlan(plan)
                .build();
        return memberRepository.save(member);
    }

    private MemberRequest createRequest() {
        return new MemberRequest(UUID.randomUUID().toString(), UUID.randomUUID() + "@test.com");
    }

    @Test
    void shouldGetAllMembers() throws Exception {
        MembershipPlan plan = createPlan(5);
        Member m1 = createMember(plan);
        Member m2 = createMember(plan);

        mockMvc.perform(get("/members")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[*].id").value(hasItems(m1.getId().toString(), m2.getId().toString())))
                .andExpect(jsonPath("$[*].fullName").value(hasItems(m1.getFullName(), m2.getFullName())));
    }

    @Test
    void shouldGetMemberById() throws Exception {
        MembershipPlan plan = createPlan(5);
        Member m1 = createMember(plan);

        mockMvc.perform(get("/members/" + m1.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(m1.getId().toString()))
                .andExpect(jsonPath("$.fullName").value(m1.getFullName()))
                .andExpect(jsonPath("$.email").value(m1.getEmail()));
    }

    @Test
    void shouldReturn404WhenMemberDoesNotExistGettingMember() throws Exception {
        UUID fakeId = UUID.randomUUID();

        mockMvc.perform(get("/members/" + fakeId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateMember() throws Exception {
        MembershipPlan plan = createPlan(5);
        MemberRequest request = createRequest();

        mockMvc.perform(post("/membershipPlans/" + plan.getId() + "/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fullName").value(request.fullName()))
                .andExpect(jsonPath("$.email").value(request.email()))
                .andExpect(jsonPath("$.status").value("ACTIVE"));

        Member saved = memberRepository.findAll().stream()
                .filter(m -> m.getEmail().equals(request.email()))
                .findFirst()
                .orElseThrow();

        assertThat(saved.getFullName()).isEqualTo(request.fullName());
        assertThat(saved.getMembershipPlan().getId()).isEqualTo(plan.getId());
    }

    @Test
    void shouldReturn400WhenMembershipPlanIsFull() throws Exception {
        MembershipPlan plan = createPlan(1);
        createMember(plan, MemberStatus.ACTIVE);

        MemberRequest request = createRequest();
        mockMvc.perform(post("/membershipPlans/" + plan.getId() + "/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenCreateMemberWithInvalidData() throws Exception {
        MembershipPlan plan = createPlan(5);
        MemberRequest request = new MemberRequest("", "");

        mockMvc.perform(post("/membershipPlans/" + plan.getId() + "/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn409WhenEmailDuplicatedCreatingMember() throws Exception {
        MembershipPlan plan = createPlan(5);
        Member member = createMember(plan, MemberStatus.ACTIVE);
        MemberRequest request = new MemberRequest(UUID.randomUUID().toString(), member.getEmail());

        mockMvc.perform(post("/membershipPlans/" + plan.getId() + "/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldReturn400WhenRequestBodyIsEmptyCreatingMember() throws Exception {
        MembershipPlan plan = createPlan(5);

        mockMvc.perform(post("/membershipPlans/" + plan.getId() + "/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString("")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpdateMember() throws Exception {
        MembershipPlan plan = createPlan(5);
        Member member = createMember(plan);
        MemberRequest request = createRequest();

        mockMvc.perform(put("/members/" + member.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value(request.fullName()))
                .andExpect(jsonPath("$.email").value(request.email()));

        Member updated = memberRepository.findById(member.getId()).orElseThrow();
        assertThat(updated.getFullName()).isEqualTo(request.fullName());
        assertThat(updated.getEmail()).isEqualTo(request.email());
    }

    @Test
    void shouldReturn404WhenMemberDoesNotExistUpdatingMember() throws Exception {
        UUID fakeId = UUID.randomUUID();
        MemberRequest request = createRequest();

        mockMvc.perform(put("/members/" + fakeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn400WhenRequestBodyHasInvalidDataUpdatingMember() throws Exception {
        MembershipPlan plan = createPlan(5);
        Member member = createMember(plan);
        MemberRequest request = new MemberRequest("", "");

        mockMvc.perform(put("/members/" + member.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn409WhenEmailDuplicatedUpdatingMember() throws Exception {
        MembershipPlan plan = createPlan(5);
        Member m1 = createMember(plan, MemberStatus.ACTIVE);
        Member m2 = createMember(plan, MemberStatus.ACTIVE);
        MemberRequest request = new MemberRequest(UUID.randomUUID().toString(), m1.getEmail());

        mockMvc.perform(put("/members/" + m2.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldReturn400WhenRequestBodyIsEmptyUpdatingMember() throws Exception {
        MembershipPlan plan = createPlan(5);
        Member member = createMember(plan);

        mockMvc.perform(put("/members/" + member.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString("")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldCancelMember() throws Exception {
        MembershipPlan plan = createPlan(5);
        Member member = createMember(plan, MemberStatus.ACTIVE);

        mockMvc.perform(patch("/members/" + member.getId() + "/cancel"))
                .andExpect(status().isNoContent());

        Member cancelled = memberRepository.findById(member.getId()).orElseThrow();
        assertThat(cancelled.getStatus()).isEqualTo(MemberStatus.CANCELLED);
    }

    @Test
    void shouldDeleteMember() throws Exception {
        MembershipPlan plan = createPlan(5);
        Member member = createMember(plan);

        mockMvc.perform(delete("/members/" + member.getId()))
                .andExpect(status().isNoContent());

        assertThat(memberRepository.existsById(member.getId())).isFalse();
    }

    @Test
    void shouldReturn404WhenMemberDoesNotExistDeletingMember() throws Exception {
        UUID fakeId = UUID.randomUUID();

        mockMvc.perform(delete("/members/" + fakeId))
                .andExpect(status().isNotFound());
    }
}