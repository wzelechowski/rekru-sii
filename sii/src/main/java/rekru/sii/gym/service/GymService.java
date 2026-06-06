package rekru.sii.gym.service;

import rekru.sii.gym.model.Gym;
import rekru.sii.gym.payload.request.GymRequest;
import rekru.sii.gym.payload.response.GymResponse;

import java.util.List;
import java.util.UUID;

public interface GymService {
    List<GymResponse> getAll();

    GymResponse getById(UUID id);

    GymResponse save(GymRequest request);

    void delete(UUID id);

    GymResponse update(GymRequest request, UUID id);

    Gym getGymEntity(UUID id);
}
