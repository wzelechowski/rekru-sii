package rekru.sii.membershipPlan.payload.response;

import rekru.sii.membershipPlan.model.MembershipPlanType;

import java.math.BigDecimal;
import java.util.UUID;

public record MembershipPlanResponse(
        UUID id,
        String name,
        MembershipPlanType type,
        BigDecimal monthlyPrice,
        String currency,
        Integer durationInMonths,
        Integer maxMembers
) {
}
