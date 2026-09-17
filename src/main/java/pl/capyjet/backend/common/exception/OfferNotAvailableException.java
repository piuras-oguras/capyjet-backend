package pl.capyjet.backend.common.exception;

public class OfferNotAvailableException extends RuntimeException {

    public OfferNotAvailableException(Long id) {
        super(String.format("Offer with ID %d is not available", id));
    }
}
