package rekru.sii.membershipPlan.mapper;

import org.springframework.stereotype.Component;
import rekru.sii.membershipPlan.model.MembershipPlan;
import rekru.sii.membershipPlan.payload.request.MembershipPlanRequest;
import rekru.sii.membershipPlan.payload.response.MembershipPlanResponse;

@Component
public class MembershipPlanMapper {
    public MembershipPlan toEntity(MembershipPlanRequest request) {
        return MembershipPlan.builder()
                .name(request.name())
                .type(request.type())
                .monthlyPrice(request.monthlyPrice())
                .currency(request.currency())
                .durationInMonths(request.durationInMonths())
                .maxMembers(request.maxMembers())
                .build();
    }

    public MembershipPlanResponse toResponse(MembershipPlan membershipPlan) {
        return new MembershipPlanResponse(
                membershipPlan.getId(),
                membershipPlan.getName(),
                membershipPlan.getType(),
                membershipPlan.getMonthlyPrice(),
                membershipPlan.getCurrency(),
                membershipPlan.getDurationInMonths(),
                membershipPlan.getMaxMembers()
        );
    }

    public void updateEntity(MembershipPlan membershipPlan, MembershipPlanRequest request) {
        if (request.name() != null) {
            membershipPlan.setName(request.name());
        }

        if (request.type() != null) {
            membershipPlan.setType(request.type());
        }

        if (request.monthlyPrice() != null) {
            membershipPlan.setMonthlyPrice(request.monthlyPrice());
        }

        if (request.currency() != null) {
            membershipPlan.setCurrency(request.currency());
        }

        if (request.durationInMonths() != null) {
            membershipPlan.setDurationInMonths(request.durationInMonths());
        }

        if (request.maxMembers() != null) {
            membershipPlan.setMaxMembers(request.maxMembers());
        }
    }
}
