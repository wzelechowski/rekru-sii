package rekru.sii.gym.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rekru.sii.gym.payload.request.GymRequest;
import rekru.sii.gym.payload.response.GymResponse;
import rekru.sii.gym.service.GymService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gyms")
public class GymController {
    private final GymService gymService;

    @GetMapping
    public ResponseEntity<List<GymResponse>> getAll() {
        return ResponseEntity.ok(gymService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GymResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(gymService.getById(id));
    }

    @PostMapping
    public ResponseEntity<GymResponse> create(@Valid @RequestBody GymRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(gymService.create(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        gymService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<GymResponse> update(@PathVariable UUID id, @Valid @RequestBody GymRequest request) {
        return ResponseEntity.ok(gymService.update(request, id));
    }
}
