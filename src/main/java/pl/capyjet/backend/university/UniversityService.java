package pl.capyjet.backend.university;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.capyjet.backend.common.exception.NotFoundException;
import pl.capyjet.backend.university.dto.CreateUniversityRequest;
import pl.capyjet.backend.university.dto.UniversityResponse;

import java.util.List;

@Service
public class UniversityService {

    private final UniversityRepository universityRepository;

    public UniversityService(UniversityRepository universityRepository) {
        this.universityRepository = universityRepository;
    }

    @Transactional(readOnly = true)
    public List<UniversityResponse> getAll() {
        return universityRepository.findAll().stream()
                .map(UniversityResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public UniversityResponse getById(Long id) {
        return UniversityResponse.from(findEntity(id));
    }

    @Transactional
    public UniversityResponse create(CreateUniversityRequest request) {
        University university = new University(request.name(), request.contactEmail(), request.contactPhone());
        return UniversityResponse.from(universityRepository.save(university));
    }

    University findEntity(Long id) {
        return universityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("University", id));
    }


}
