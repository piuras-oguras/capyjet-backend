package pl.capyjet.backend.offer;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Long> {

    @EntityGraph(attributePaths = "university")
    List<Offer> findAllByOrderByCreatedAtDesc();

    @EntityGraph(attributePaths = "university")
    List<Offer> findAllByStatusOrderByCreatedAtDesc(OfferStatus status);
}
