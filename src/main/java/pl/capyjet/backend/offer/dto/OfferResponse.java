package pl.capyjet.backend.offer.dto;

import pl.capyjet.backend.offer.Offer;
import pl.capyjet.backend.offer.OfferStatus;

import java.time.LocalDateTime;

public record OfferResponse (
        Long id,
        String title,
        String description,
        OfferStatus status,
        String universityName,
        LocalDateTime createdAt
){
    public static OfferResponse from(Offer offer) {
        return new OfferResponse(
                offer.getId(),
                offer.getTitle(),
                offer.getDescription(),
                offer.getStatus(),
                offer.getUniversity().getName(),
                offer.getCreatedAt()
        );
    }
}
