package com.bibliotheque.repositories;

import com.bibliotheque.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import java.lang.StackWalker.Option;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    // Récupère les réservations par adhérent
    List<Reservation> findByAdherentId(Integer idAdherent);

    // Récupère les réservations par livre
    List<Reservation> findByLivreId(Integer idLivre);

    // Récupère les réservations par statut
    List<Reservation> findByStatutId(Integer idStatut);

    public Optional<Reservation> findById(Integer id);
}
