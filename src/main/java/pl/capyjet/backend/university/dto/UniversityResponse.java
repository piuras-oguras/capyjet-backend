package pl.capyjet.backend.university.dto;

import pl.capyjet.backend.university.University;

public record UniversityResponse(Long id, String name) {

    public static UniversityResponse from(University university) {
        return new UniversityResponse(university.getId(), university.getName());
    }
}
