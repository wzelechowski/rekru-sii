package rekru.sii.member.service;

import rekru.sii.member.payload.request.MemberRequest;
import rekru.sii.member.payload.response.MemberResponse;

import java.util.List;
import java.util.UUID;

public interface MemberService {

    List<MemberResponse> getAll();

    MemberResponse getById(UUID id);

    MemberResponse save(UUID membershipPlanId, MemberRequest request);

    void delete(UUID id);

    MemberResponse update(MemberRequest request, UUID id);

    void cancel(UUID id);
}
