package com.bibliotheque.repositories;

import com.bibliotheque.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

   
    public List<Reservation> findByAdherentId(Integer adherentId);

 
    public List<Reservation> findByLivreId(Integer livreId);


    public Optional<Reservation> findById(Integer id);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.livre.id = :livreId AND r.dateAReserver = :date AND r.id IN (SELECT m.reservation.id FROM MvtReservation m WHERE m.statutNouveau.codeStatut IN ('En attente', 'Confirmée'))")
    public int countActiveReservationsByLivreAndDate(@Param("livreId") Integer livreId, @Param("date") LocalDate date);

    @Query(value = """
        SELECT COUNT(*) FROM reservations r
        WHERE r.id_adherent = :adherentId
          AND (
            SELECT sr.code_statut
            FROM mvt_reservation m
            JOIN statuts_reservation sr ON m.id_statut_nouveau = sr.id_statut
            WHERE m.id_reservation = r.id_reservation
            ORDER BY m.date_mouvement DESC
            LIMIT 1
          ) IN ('En attente', 'Confirmée')
        """, nativeQuery = true)
    long countActiveReservationsByAdherent(@Param("adherentId") Integer adherentId);


    
}