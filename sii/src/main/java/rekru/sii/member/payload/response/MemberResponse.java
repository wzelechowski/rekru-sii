package rekru.sii.member.payload.response;

import rekru.sii.member.model.MemberStatus;

import java.time.LocalDate;
import java.util.UUID;

public record MemberResponse(
        UUID id,
        String fullName,
        String email,
        LocalDate startDate,
        MemberStatus status
) {
}
