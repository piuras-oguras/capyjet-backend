package pl.capyjet.backend.offer;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.capyjet.backend.common.exception.NotFoundException;
import pl.capyjet.backend.common.exception.OfferNotAvailableException;
import pl.capyjet.backend.offer.dto.AcceptedOfferResponse;
import pl.capyjet.backend.offer.dto.OfferRequest;
import pl.capyjet.backend.offer.dto.OfferResponse;
import pl.capyjet.backend.university.University;
import pl.capyjet.backend.university.UniversityRepository;

import java.util.List;

@Service
public class OfferService {
    private final OfferRepository offerRepository;
    private final UniversityRepository universityRepository;

    public OfferService(OfferRepository offerRepository, UniversityRepository universityRepository) {
        this.offerRepository = offerRepository;
        this.universityRepository = universityRepository;
    }

    @Transactional(readOnly = true)
    public List<OfferResponse> getAll(OfferStatus status){
        List<Offer> offers = (status == null)
                ? offerRepository.findAllByOrderByCreatedAtDesc()
                : offerRepository.findAllByStatusOrderByCreatedAtDesc(status);
        return offers.stream()
                .map(OfferResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public OfferResponse getById(Long id) {
        return OfferResponse.from(findOffer(id));
    }

    @Transactional
    public OfferResponse create(OfferRequest request) {
        University university = findUniversity(request.universityId());
        Offer offer = new Offer(request.title(), request.description(), university);
        return OfferResponse.from(offerRepository.save(offer));
    }

    @Transactional
    public OfferResponse update(Long id, OfferRequest request) {
        Offer offer = findOffer(id);
        offer.setTitle(request.title());
        offer.setDescription(request.description());
        offer.setUniversity(findUniversity(request.universityId()));
        return OfferResponse.from(offer);
    }

    @Transactional
    public void delete(Long id){
        if (!offerRepository.existsById(id)) {
            throw new NotFoundException("Offer", id);
        }
        offerRepository.deleteById(id);
    }

    @Transactional
    public AcceptedOfferResponse accept(Long id){
        Offer offer = findOffer(id);
        if (!offer.isOpen()) {
            throw new OfferNotAvailableException(id);
        }
        offer.accept();
        return AcceptedOfferResponse.from(offer);
    }

    private Offer findOffer(Long id) {
        return offerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Offer", id));
    }

    private University findUniversity(Long id) {
        return universityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("University", id));
    }



}
