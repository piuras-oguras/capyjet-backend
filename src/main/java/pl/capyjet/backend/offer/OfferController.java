package pl.capyjet.backend.offer;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.capyjet.backend.offer.dto.AcceptedOfferResponse;
import pl.capyjet.backend.offer.dto.OfferRequest;
import pl.capyjet.backend.offer.dto.OfferResponse;

import java.util.List;

@RestController
@RequestMapping("/api/offers")
public class OfferController {
    private final OfferService offerService;

    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @GetMapping
    public List<OfferResponse> getAll(@RequestParam(required = false) OfferStatus status) {
        return offerService.getAll(status);
    }

    @GetMapping("/{id}")
    public OfferResponse getById(@PathVariable Long id) {
        return offerService.getById(id);
    }

    @PostMapping
    public ResponseEntity<OfferResponse> create(@Valid @RequestBody OfferRequest Request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(offerService.create(Request));
    }

    @PutMapping("/{id}")
    public OfferResponse update(@PathVariable Long id, @Valid @RequestBody OfferRequest Request) {
        return offerService.update(id, Request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        offerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/accept")
    public AcceptedOfferResponse accept(@PathVariable Long id) {
        return offerService.accept(id);
    }
}
