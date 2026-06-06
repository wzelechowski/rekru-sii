package rekru.sii.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rekru.sii.member.payload.request.MemberRequest;
import rekru.sii.member.payload.response.MemberResponse;
import rekru.sii.member.service.MemberService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/members")
    public ResponseEntity<List<MemberResponse>> getAll() {
        return ResponseEntity.ok(memberService.getAll());
    }

    @GetMapping("/members/{id}")
    public ResponseEntity<MemberResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(memberService.getById(id));
    }

    @PostMapping("/membershipPlans/{membershipPlanId}/members")
    public ResponseEntity<MemberResponse> save(@PathVariable UUID membershipPlanId, @Valid @RequestBody MemberRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.save(membershipPlanId, request));
    }

    @DeleteMapping("/members/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        memberService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/members/{id}")
    public ResponseEntity<MemberResponse> update(@Valid @RequestBody MemberRequest request, @PathVariable UUID id) {
        return ResponseEntity.ok(memberService.update(request, id));
    }

    @PatchMapping("/members/{id}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable UUID id) {
        memberService.cancel(id);
        return ResponseEntity.noContent().build();
    }
}
