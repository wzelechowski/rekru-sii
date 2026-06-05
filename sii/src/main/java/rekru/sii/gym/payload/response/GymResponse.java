package rekru.sii.gym.payload.response;

import java.util.UUID;

public record GymResponse(
        UUID id,
        String name,
        String address,
        String phoneNumber
) {
}
