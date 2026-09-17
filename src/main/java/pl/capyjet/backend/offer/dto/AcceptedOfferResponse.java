package pl.capyjet.backend.offer.dto;

import pl.capyjet.backend.offer.Offer;
import pl.capyjet.backend.offer.OfferStatus;

public record AcceptedOfferResponse(long offerId, OfferStatus status, Contact contact) {
    public record Contact(String universityName, String email, String phone){
    }

    public static AcceptedOfferResponse from(Offer offer){
        var university = offer.getUniversity();
        return new AcceptedOfferResponse(
                offer.getId(),
                offer.getStatus(),
                new Contact(
                        university.getName(),
                        university.getContactEmail(),
                        university.getContactPhone()
                )
        );
    }
}
