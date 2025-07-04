package com.bibliotheque.repositories;

import com.bibliotheque.entities.StatutReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface StatutReservationRepository extends JpaRepository<StatutReservation, Integer> {
    // Tu peux ajouter ici des méthodes personnalisées si besoin, par exemple :
    public Optional<StatutReservation> findByCodeStatut(String codeStatut);
    // Optional<StatutReservation> findByCodeStatut(String codeStatut);
}
