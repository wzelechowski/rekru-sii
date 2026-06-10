package rekru.sii.membershipPlan;

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
import rekru.sii.membershipPlan.model.MembershipPlan;
import rekru.sii.membershipPlan.model.MembershipPlanType;
import rekru.sii.membershipPlan.payload.request.MembershipPlanRequest;
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
public class MembershipPlanControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MembershipPlanRepository membershipPlanRepository;

    @Autowired
    private GymRepository gymRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Gym createGym() {
        Gym gym = Gym.builder()
                .name(UUID.randomUUID().toString())
                .address("address")
                .phoneNumber("123456789")
                .build();
        return gymRepository.save(gym);
    }

    private MembershipPlan createPlan(Gym gym) {
        MembershipPlan plan = MembershipPlan.builder()
                .name(UUID.randomUUID().toString())
                .type(MembershipPlanType.PREMIUM)
                .monthlyPrice(new BigDecimal("150.00"))
                .currency("PLN")
                .durationInMonths(12)
                .maxMembers(50)
                .gym(gym)
                .build();
        return membershipPlanRepository.save(plan);
    }

    private MembershipPlanRequest createRequest() {
        return new MembershipPlanRequest(
                UUID.randomUUID().toString(),
                MembershipPlanType.BASIC,
                new BigDecimal("100.00"),
                "PLN",
                6,
                30
        );
    }

    @Test
    void shouldGetAllMembershipPlans() throws Exception {
        Gym gym = createGym();
        MembershipPlan p1 = createPlan(gym);
        MembershipPlan p2 = createPlan(gym);

        mockMvc.perform(get("/membershipPlans")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[*].id").value(hasItems(p1.getId().toString(), p2.getId().toString())))
                .andExpect(jsonPath("$[*].name").value(hasItems(p1.getName(), p2.getName())));
    }

    @Test
    void shouldGetMembershipPlansByGymId() throws Exception {
        Gym gym1 = createGym();
        Gym gym2 = createGym();
        MembershipPlan p1 = createPlan(gym1);
        MembershipPlan p2 = createPlan(gym2);

        mockMvc.perform(get("/gyms/" + gym1.getId() + "/membershipPlans")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id").value(hasItems(p1.getId().toString())))
                .andExpect(jsonPath("$[*].id").value(org.hamcrest.Matchers.not(hasItems(p2.getId().toString()))));
    }

    @Test
    void shouldGetMembershipPlanById() throws Exception {
        Gym gym = createGym();
        MembershipPlan plan = createPlan(gym);

        mockMvc.perform(get("/membershipPlans/" + plan.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(plan.getId().toString()))
                .andExpect(jsonPath("$.name").value(plan.getName()))
                .andExpect(jsonPath("$.monthlyPrice").value(plan.getMonthlyPrice().doubleValue()));
    }

    @Test
    void shouldReturn404WhenMembershipPlanDoesNotExistGettingMembershipPlan() throws Exception {
        UUID fakeId = UUID.randomUUID();

        mockMvc.perform(get("/membershipPlans/" + fakeId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateMembershipPlan() throws Exception {
        Gym gym = createGym();
        MembershipPlanRequest request = createRequest();

        mockMvc.perform(post("/gyms/" + gym.getId() + "/membershipPlans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(request.name()))
                .andExpect(jsonPath("$.currency").value(request.currency()));

        MembershipPlan saved = membershipPlanRepository.findAll().stream()
                .filter(p -> p.getName().equals(request.name()))
                .findFirst()
                .orElseThrow();

        assertThat(saved.getGym().getId()).isEqualTo(gym.getId());
        assertThat(saved.getMaxMembers()).isEqualTo(request.maxMembers());
        assertThat(saved.getMonthlyPrice()).isEqualByComparingTo(request.monthlyPrice());
    }

    @Test
    void shouldReturn400WhenRequestBodyHasInvalidDataCreatingMembershipPlan() throws Exception {
        Gym gym = createGym();
        MembershipPlanRequest request = new MembershipPlanRequest(
                "", null, new BigDecimal("-10"), "INVALID", 0, 0
        );

        mockMvc.perform(post("/gyms/" + gym.getId() + "/membershipPlans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenRequestBodyIsEmptyCreatingMembershipPlan() throws Exception {
        Gym gym = createGym();

        mockMvc.perform(post("/gyms/" + gym.getId() + "/membershipPlans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString("")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpdateMembershipPlan() throws Exception {
        Gym gym = createGym();
        MembershipPlan plan = createPlan(gym);
        MembershipPlanRequest request = createRequest();

        mockMvc.perform(put("/membershipPlans/" + plan.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(request.name()))
                .andExpect(jsonPath("$.monthlyPrice").value(request.monthlyPrice().doubleValue()));

        MembershipPlan updated = membershipPlanRepository.findById(plan.getId()).orElseThrow();
        assertThat(updated.getName()).isEqualTo(request.name());
        assertThat(updated.getMonthlyPrice()).isEqualByComparingTo(request.monthlyPrice());
    }

    @Test
    void shouldReturn404WhenMembershipPlanDoesNotExistUpdatingMembershipPlan() throws Exception {
        UUID fakeId = UUID.randomUUID();
        MembershipPlanRequest request = createRequest();

        mockMvc.perform(put("/membershipPlans/" + fakeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn400WhenRequestBodyHasInvalidDataUpdatingMembershipPlan() throws Exception {
        Gym gym = createGym();
        MembershipPlan plan = createPlan(gym);
        MembershipPlanRequest request = new MembershipPlanRequest(
                "", null, new BigDecimal("-10"), "INVALID", 0, 0
        );

        mockMvc.perform(put("/membershipPlans/" + plan.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenRequestBodyIsEmptyUpdatingMembershipPlan() throws Exception {
        Gym gym = createGym();
        MembershipPlan plan = createPlan(gym);

        mockMvc.perform(put("/membershipPlans/" + plan.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString("")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldDeleteMembershipPlan() throws Exception {
        Gym gym = createGym();
        MembershipPlan plan = createPlan(gym);

        mockMvc.perform(delete("/membershipPlans/" + plan.getId()))
                .andExpect(status().isNoContent());

        assertThat(membershipPlanRepository.existsById(plan.getId())).isFalse();
    }

    @Test
    void shouldReturn404WhenMembershipPlanDoesNotExistDeletingMembershipPlan() throws Exception {
        UUID fakeId = UUID.randomUUID();

        mockMvc.perform(delete("/membershipPlans/" + fakeId))
                .andExpect(status().isNotFound());
    }
}