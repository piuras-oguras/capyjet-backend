package pl.capyjet.backend.offer;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.capyjet.backend.university.University;

import java.time.LocalDateTime;

@Entity
@Table(name = "offers")
@Getter
@Setter
@NoArgsConstructor
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 5000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OfferStatus status = OfferStatus.OPEN;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "university_id", nullable = false)
    private University university;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Offer(String title, String description, University university) {
        this.title = title;
        this.description = description;
        this.university = university;
    }

    @PrePersist
    void onCreate(){
        createdAt = LocalDateTime.now();
    }

    public boolean isOpen(){
        return status == OfferStatus.OPEN;
    }
    
    public void accept(){
        status = OfferStatus.ACCEPTED;
    }
}
