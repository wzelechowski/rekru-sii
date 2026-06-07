package rekru.sii.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import rekru.sii.member.mapper.MemberMapper;
import rekru.sii.member.model.Member;
import rekru.sii.member.model.MemberStatus;
import rekru.sii.member.payload.request.MemberRequest;
import rekru.sii.member.payload.response.MemberResponse;
import rekru.sii.member.repository.MemberRepository;
import rekru.sii.membershipPlan.model.MembershipPlan;
import rekru.sii.membershipPlan.service.MembershipPlanService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final MembershipPlanService membershipPlanService;

    @Override
    public List<MemberResponse> getAll() {
        return memberRepository.findAll()
                .stream()
                .map(memberMapper::toResponse)
                .toList();
    }

    @Override
    public MemberResponse getById(UUID id) {
        Member member = getMemberEntity(id);
        return memberMapper.toResponse(member);
    }

    @Override
    @Transactional
    public MemberResponse save(UUID membershipPlanId, MemberRequest request) {
        long activeMembersCount = memberRepository.countByMembershipPlanIdAndStatus(
                membershipPlanId, MemberStatus.ACTIVE
        );

        MembershipPlan membershipPlan = membershipPlanService.getMembershipPlanEntityWithGymInfo(membershipPlanId);
        if (activeMembersCount >= membershipPlan.getMaxMembers()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Membership plan has maximum number of members");
        }

        Member member = memberMapper.toEntity(request);
        member.setMembershipPlan(membershipPlan);
        Member savedMember = memberRepository.save(member);
        return memberMapper.toResponse(savedMember);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Member member = getMemberEntity(id);
        memberRepository.delete(member);
    }

    @Override
    @Transactional
    public MemberResponse update(MemberRequest request, UUID id) {
        Member member = getMemberEntity(id);
        memberMapper.updateEntity(member, request);
        return memberMapper.toResponse(member);
    }

    @Override
    @Transactional
    public void cancel(UUID id) {
        Member member = getMemberEntity(id);
        member.changeMemberStatus(MemberStatus.CANCELLED);
    }

    private Member getMemberEntity(UUID id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found"));
    }
}
