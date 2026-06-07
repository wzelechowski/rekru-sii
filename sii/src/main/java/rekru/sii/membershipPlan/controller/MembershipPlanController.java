package rekru.sii.membershipPlan.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rekru.sii.membershipPlan.payload.request.MembershipPlanRequest;
import rekru.sii.membershipPlan.payload.response.MembershipPlanResponse;
import rekru.sii.membershipPlan.service.MembershipPlanService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class MembershipPlanController {
    private final MembershipPlanService membershipPlanService;

    @GetMapping("/membershipPlans")
    public ResponseEntity<List<MembershipPlanResponse>> getAll() {
        return ResponseEntity.ok(membershipPlanService.getAll());
    }

    @GetMapping("/gyms/{gymId}/membershipPlans")
    public ResponseEntity<List<MembershipPlanResponse>> getByGymId(@PathVariable UUID gymId) {
        return ResponseEntity.ok(membershipPlanService.getByGymId(gymId));
    }

    @GetMapping("/membershipPlans/{id}")
    public ResponseEntity<MembershipPlanResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(membershipPlanService.getById(id));
    }

    @PostMapping("/gyms/{gymId}/membershipPlans")
    public ResponseEntity<MembershipPlanResponse> save(@PathVariable UUID gymId, @Valid @RequestBody MembershipPlanRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(membershipPlanService.save(gymId, request));
    }

    @DeleteMapping("/membershipPlans/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        membershipPlanService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/membershipPlans/{id}")
    public ResponseEntity<MembershipPlanResponse> update(@Valid @RequestBody MembershipPlanRequest request, @PathVariable UUID id) {
        return ResponseEntity.ok(membershipPlanService.update(request, id));
    }
}
