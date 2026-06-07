package rekru.sii.member.mapper;

import org.springframework.stereotype.Component;
import rekru.sii.member.model.Member;
import rekru.sii.member.payload.request.MemberRequest;
import rekru.sii.member.payload.response.MemberResponse;

@Component
public class MemberMapper {

    public Member toEntity(MemberRequest request) {
        return Member.builder()
                .fullName(request.fullName())
                .email(request.email())
                .build();
    }

    public MemberResponse toResponse(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getFullName(),
                member.getEmail(),
                member.getStartDate(),
                member.getStatus(),
                member.getMembershipPlan().getName(),
                member.getMembershipPlan().getGym().getName()
        );
    }

    public void updateEntity(Member member, MemberRequest request) {
        if (request.fullName() != null) {
            member.setFullName(request.fullName());
        }

        if (request.email() != null) {
            member.setEmail(request.email());
        }
    }
}
