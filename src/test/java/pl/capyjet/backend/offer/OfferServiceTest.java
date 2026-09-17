package pl.capyjet.backend.offer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.capyjet.backend.common.exception.NotFoundException;
import pl.capyjet.backend.common.exception.OfferNotAvailableException;
import pl.capyjet.backend.offer.dto.AcceptedOfferResponse;
import pl.capyjet.backend.offer.dto.OfferRequest;
import pl.capyjet.backend.university.University;
import pl.capyjet.backend.university.UniversityRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OfferServiceTest {

    @Mock
    private OfferRepository offerRepository;

    @Mock
    private UniversityRepository universityRepository;

    @InjectMocks
    private OfferService offerService;

    private final University university =
            new University("Bydgoszcz University of Technology", "contact@pbs.edu.pl", "123456789");

    @Test
    void shouldThrowWhenOfferNotFound() {
        when(offerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> offerService.getById(1L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void shouldAcceptOpenOfferAndRevealContact() {
        Offer offer = new Offer("Traffic prediction", "Description", university);
        offer.setId(1L);
        when(offerRepository.findById(1L)).thenReturn(Optional.of(offer));

        AcceptedOfferResponse response = offerService.accept(1L);

        assertThat(response.status()).isEqualTo(OfferStatus.ACCEPTED);
        assertThat(response.contact().email()).isEqualTo("contact@pbs.edu.pl");
    }

    @Test
    void shouldNotAcceptOfferThatIsNotOpen() {
        Offer offer = new Offer("Traffic prediction", "Description", university);
        offer.accept();
        when(offerRepository.findById(1L)).thenReturn(Optional.of(offer));

        assertThatThrownBy(() -> offerService.accept(1L))
                .isInstanceOf(OfferNotAvailableException.class);
    }

    @Test
    void shouldNotCreateOfferForMissingUniversity() {
        when(universityRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> offerService.create(new OfferRequest("Title", "Description", 99L)))
                .isInstanceOf(NotFoundException.class);
        verify(offerRepository, never()).save(any());
    }
}
