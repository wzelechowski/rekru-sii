package rekru.sii.membershipPlan.payload.request;

import jakarta.validation.constraints.*;
import rekru.sii.membershipPlan.model.MembershipPlanType;

import java.math.BigDecimal;

public record MembershipPlanRequest(
        @NotBlank
        String name,

        @NotNull
        MembershipPlanType type,

        @NotNull
        @Positive
        BigDecimal monthlyPrice,

        @NotBlank
        @Pattern(regexp = "^[A-Z]{3}$")
        String currency,

        @NotNull
        @Min(1)
        Integer durationInMonths,

        @NotNull
        @Min(1)
        Integer maxMembers
) {
}
