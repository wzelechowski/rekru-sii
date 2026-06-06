package rekru.sii.membershipPlan.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import rekru.sii.membershipPlan.model.MembershipPlanType;

import java.math.BigDecimal;

public record MembershipPlanRequest(
        @NotBlank
        String name,

        @NotNull
        MembershipPlanType type,

        @NotNull
        BigDecimal monthlyPrice,

        @NotBlank
        @Pattern(regexp = "^[A-Z]{3}$")
        String currency,

        @NotNull
        Integer durationInMonths,

        @NotNull
        Integer maxMembers
) {
}
