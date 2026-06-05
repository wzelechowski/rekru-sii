package rekru.sii.gym.payload.request;

import jakarta.validation.constraints.NotBlank;

public record GymRequest(
        @NotBlank
        String name,

        @NotBlank
        String address,

        @NotBlank
        String phoneNumber
) {
}
