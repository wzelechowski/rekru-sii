package rekru.sii.membershipPlan.service;

import rekru.sii.membershipPlan.model.MembershipPlan;
import rekru.sii.membershipPlan.payload.request.MembershipPlanRequest;
import rekru.sii.membershipPlan.payload.response.MembershipPlanResponse;

import java.util.List;
import java.util.UUID;

public interface MembershipPlanService {
    List<MembershipPlanResponse> getAll();

    List<MembershipPlanResponse> getByGymId(UUID gymId);

    MembershipPlanResponse getById(UUID id);

    MembershipPlanResponse save(UUID gymId, MembershipPlanRequest request);

    void delete(UUID id);

    MembershipPlanResponse update(MembershipPlanRequest request, UUID id);

    MembershipPlan getMembershipPlanEntity(UUID id);

    MembershipPlan getMembershipPlanEntityWithGymInfo(UUID id);
}
