package com.bibliotheque.repositories;

import com.bibliotheque.entities.MvtReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface MvtReservationRepository extends JpaRepository<MvtReservation, Integer> {

    // Exemple : récupérer tous les mouvements d'une réservation donnée
    List<MvtReservation> findByReservationId(Integer reservationId);

    public Optional<MvtReservation> findTopByReservationIdOrderByDateMouvementDesc(Integer reservationId);
}
