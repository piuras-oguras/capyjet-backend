package pl.capyjet.backend.university;


import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.capyjet.backend.university.dto.CreateUniversityRequest;
import pl.capyjet.backend.university.dto.UniversityResponse;

import java.util.List;

@RestController
@RequestMapping("api/universities")
public class UniversityController {
    private final UniversityService universityService;

    public UniversityController(UniversityService universityService) {
        this.universityService = universityService;
    }

    @GetMapping
    public List<UniversityResponse> getAll() {
        return universityService.getAll();
    }

    @GetMapping("/{id}")
    public UniversityResponse getById(@PathVariable Long id) {
        return universityService.getById(id);
    }

    @PostMapping
    public ResponseEntity<UniversityResponse> create(@Valid @RequestBody CreateUniversityRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(universityService.create(request));
    }


}
