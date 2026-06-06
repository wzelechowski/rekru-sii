package rekru.sii.gym.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import rekru.sii.gym.mapper.GymMapper;
import rekru.sii.gym.model.Gym;
import rekru.sii.gym.payload.request.GymRequest;
import rekru.sii.gym.payload.response.GymResponse;
import rekru.sii.gym.repository.GymRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GymServiceImpl implements GymService {
    private final GymRepository gymRepository;
    private final GymMapper gymMapper;

    @Override
    public List<GymResponse> getAll() {
        return gymRepository.findAll()
                .stream()
                .map(gymMapper::toResponse)
                .toList();
    }

    @Override
    public GymResponse getById(UUID id) {
        Gym gym = getGymEntity(id);
        return gymMapper.toResponse(gym);
    }

    @Override
    @Transactional
    public GymResponse save(GymRequest request) {
        Gym gym = gymMapper.toEntity(request);
        Gym savedGym = gymRepository.save(gym);
        return gymMapper.toResponse(savedGym);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Gym gym = getGymEntity(id);
        gymRepository.delete(gym);
    }

    @Override
    @Transactional
    public GymResponse update(GymRequest request, UUID id) {
        Gym gym = getGymEntity(id);
        gymMapper.updateEntity(gym, request);
        return gymMapper.toResponse(gym);
    }

    @Override
    public Gym getGymEntity(UUID id) {
        return gymRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gym not found"));
    }
}
