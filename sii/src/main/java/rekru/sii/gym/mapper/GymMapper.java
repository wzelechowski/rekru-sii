package rekru.sii.gym.mapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import rekru.sii.gym.model.Gym;
import rekru.sii.gym.payload.request.GymRequest;
import rekru.sii.gym.payload.response.GymResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class GymMapper {
    public Gym toEntity(GymRequest request) {
        return Gym.builder()
                .name(request.name())
                .address(request.address())
                .phoneNumber(request.phoneNumber())
                .build();
    }

    public GymResponse toResponse(Gym gym) {
        return new GymResponse(
                gym.getId(),
                gym.getName(),
                gym.getAddress(),
                gym.getPhoneNumber()
        );
    }

    public void updateEntity(Gym gym, GymRequest request) {
        if (request.name() != null) {
            gym.setName(request.name());
        }

        if (request.address() != null) {
            gym.setAddress(request.address());
        }

        if (request.phoneNumber() != null) {
            gym.setPhoneNumber(request.phoneNumber());
        }
    }
}
