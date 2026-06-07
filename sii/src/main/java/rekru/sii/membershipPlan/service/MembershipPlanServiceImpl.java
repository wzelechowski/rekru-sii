package rekru.sii.membershipPlan.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import rekru.sii.gym.model.Gym;
import rekru.sii.gym.service.GymService;
import rekru.sii.membershipPlan.mapper.MembershipPlanMapper;
import rekru.sii.membershipPlan.model.MembershipPlan;
import rekru.sii.membershipPlan.payload.request.MembershipPlanRequest;
import rekru.sii.membershipPlan.payload.response.MembershipPlanResponse;
import rekru.sii.membershipPlan.repository.MembershipPlanRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MembershipPlanServiceImpl implements MembershipPlanService {
    private final MembershipPlanRepository membershipPlanRepository;
    private final MembershipPlanMapper membershipPlanMapper;
    private final GymService gymService;

    @Override
    public List<MembershipPlanResponse> getAll() {
        return membershipPlanRepository.findAll()
                .stream()
                .map(membershipPlanMapper::toResponse)
                .toList();
    }

    @Override
    public MembershipPlanResponse getById(UUID id) {
        MembershipPlan membershipPlan = getMembershipPlanEntity(id);
        return membershipPlanMapper.toResponse(membershipPlan);
    }

    @Override
    @Transactional
    public MembershipPlanResponse save(UUID gymId, MembershipPlanRequest request) {
        MembershipPlan membershipPlan = membershipPlanMapper.toEntity(request);
        Gym gym = gymService.getGymEntity(gymId);
        gym.addMembershipPlan(membershipPlan);
        MembershipPlan savedMembershipPlan = membershipPlanRepository.save(membershipPlan);
        return membershipPlanMapper.toResponse(savedMembershipPlan);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        MembershipPlan membershipPlan = getMembershipPlanEntity(id);
        membershipPlanRepository.delete(membershipPlan);
    }

    @Override
    @Transactional
    public MembershipPlanResponse update(MembershipPlanRequest request, UUID id) {
        MembershipPlan membershipPlan = getMembershipPlanEntity(id);
        membershipPlanMapper.updateEntity(membershipPlan, request);
        return membershipPlanMapper.toResponse(membershipPlan);
    }

    @Override
    public MembershipPlan getMembershipPlanEntity(UUID id) {
        return membershipPlanRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Membership plan not found"));
    }

    @Override
    public MembershipPlan getMembershipPlanEntityWithGymInfo(UUID id) {
        return membershipPlanRepository.getMembershipPlanWithGymById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Membership plan not found"));
    }
}
