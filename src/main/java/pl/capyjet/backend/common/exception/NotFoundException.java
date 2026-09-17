package pl.capyjet.backend.common.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String resource, Long id) {
        super(String.format("Resource %s with ID %d not found", resource, id));
    }
}
