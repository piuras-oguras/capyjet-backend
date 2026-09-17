package pl.capyjet.backend.university.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUniversityRequest(
        @NotBlank @Size(max = 200) String name,
        @NotBlank @Email String contactEmail,
        @Size(max = 20) String contactPhone) {
}
