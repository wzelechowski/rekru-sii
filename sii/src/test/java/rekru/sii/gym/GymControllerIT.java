package rekru.sii.gym;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import rekru.sii.gym.model.Gym;
import rekru.sii.gym.payload.request.GymRequest;
import rekru.sii.gym.repository.GymRepository;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class GymControllerIT {
    @Autowired
    private MockMvc mockMvc;
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

    private GymRequest createRequest() {
        return new GymRequest(UUID.randomUUID().toString(), "address", "123456789");
    }

    @Test
    void shouldGetAllGyms() throws Exception {
        Gym g1 = createGym();
        Gym g2 = createGym();

        mockMvc.perform(get("/gyms")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[*].id").value(hasItems(g1.getId().toString(), g2.getId().toString())))
                .andExpect(jsonPath("$[*].name").value(hasItems(g1.getName(), g2.getName())))
                .andExpect(jsonPath("$[*].address").value(hasItems(g1.getAddress(), g2.getAddress())))
                .andExpect(jsonPath("$[*].phoneNumber").value(hasItems(g1.getPhoneNumber(), g2.getPhoneNumber())));
    }

    @Test
    void shouldGetGymById() throws Exception {
        Gym g1 = createGym();

        mockMvc.perform(get("/gyms/" + g1.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(g1.getId().toString()))
                .andExpect(jsonPath("$.name").value(g1.getName()))
                .andExpect(jsonPath("$.address").value(g1.getAddress()))
                .andExpect(jsonPath("$.phoneNumber").value(g1.getPhoneNumber()));
    }

    @Test
    void shouldReturn404WhenGymDoesNotExistsGettingGym() throws Exception {
        UUID fakeId = UUID.randomUUID();

        mockMvc.perform(get("/gyms/" + fakeId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateGym() throws Exception {
        GymRequest request = createRequest();

        mockMvc.perform(post("/gyms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(request.name()))
                .andExpect(jsonPath("$.address").value(request.address()))
                .andExpect(jsonPath("$.phoneNumber").value(request.phoneNumber()));

        Gym saved = gymRepository.findAll().stream()
                .filter(g -> g.getName().equals(request.name()))
                .findFirst()
                .orElseThrow();

        assertThat(saved.getAddress()).isEqualTo(request.address());
        assertThat(saved.getPhoneNumber()).isEqualTo(request.phoneNumber());
    }

    @Test
    void shouldReturn400WhenRequestBodyHasInvalidDataCreatingGym() throws Exception {
        GymRequest request = new GymRequest("", "", "");

        mockMvc.perform(post("/gyms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn409WhenNameDuplicatedCreatingGym() throws Exception {
        Gym gym = createGym();
        gym.setName("g1");
        gymRepository.save(gym);
        String randomString = UUID.randomUUID().toString();
        GymRequest request = new GymRequest("g1", randomString, randomString);

        mockMvc.perform(post("/gyms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldReturn400WhenRequestBodyIsEmptyCreatingGym() throws Exception {
        mockMvc.perform(post("/gyms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString("")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpdateGym() throws Exception {
        Gym gym = createGym();
        GymRequest request = createRequest();

        mockMvc.perform(put("/gyms/" + gym.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(request.name()))
                .andExpect(jsonPath("$.address").value(request.address()))
                .andExpect(jsonPath("$.phoneNumber").value(request.phoneNumber()));

        Gym updated = gymRepository.findById(gym.getId()).orElseThrow();
        assertThat(updated.getName()).isEqualTo(request.name());
        assertThat(updated.getAddress()).isEqualTo(request.address());
        assertThat(updated.getPhoneNumber()).isEqualTo(request.phoneNumber());
    }

    @Test
    void shouldReturn404WhenGymDoesNotExistsUpdatingGym() throws Exception {
        UUID fakeId = UUID.randomUUID();
        GymRequest request = createRequest();

        mockMvc.perform(put("/gyms/" + fakeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn400WhenRequestBodyHasInvalidDataUpdatingGym() throws Exception {
        Gym gym = createGym();
        GymRequest request = new GymRequest("", "", "");

        mockMvc.perform(put("/gyms/" + gym.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn409WhenNameDuplicatedUpdatingGym() throws Exception {
        Gym g1 = createGym();
        Gym g2 = createGym();
        String randomString = UUID.randomUUID().toString();
        GymRequest request = new GymRequest(g1.getName(), randomString, randomString);

        mockMvc.perform(put("/gyms/" + g2.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldReturn400WhenRequestBodyIsEmptyUpdatingGym() throws Exception {
        Gym gym = createGym();

        mockMvc.perform(put("/gyms/" + gym.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString("")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldDeleteGym() throws Exception {
        Gym gym = createGym();

        mockMvc.perform(delete("/gyms/" + gym.getId()))
                .andExpect(status().isNoContent());

        assertThat(gymRepository.existsById(gym.getId())).isFalse();
    }

    @Test
    void shouldReturn404WhenGymDoesNotExistsDeletingGym() throws Exception {
        UUID fakeId = UUID.randomUUID();

        mockMvc.perform(delete("/gyms/" + fakeId))
                .andExpect(status().isNotFound());
    }
}
