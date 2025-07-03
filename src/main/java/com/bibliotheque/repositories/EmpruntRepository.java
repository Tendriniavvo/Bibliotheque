package com.bibliotheque.repositories;

import com.bibliotheque.entities.Emprunt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface EmpruntRepository extends JpaRepository<Emprunt, Integer> {

    // Compte les emprunts en cours pour un adhérent donné
    @Query("SELECT COUNT(e) FROM Emprunt e " +
           "WHERE e.idAdherent = :idAdherent " +
           "AND e.dateRetourPrevue > :currentDateTime " +
           "AND EXISTS (SELECT m FROM MvtEmprunt m " +
           "WHERE m.idEmprunt = e.idEmprunt " +
           "AND m.idStatutNouveau = 1)")
    long countByIdAdherentAndStatutEnCours(@Param("idAdherent") Integer idAdherent, 
                                           @Param("currentDateTime") LocalDateTime currentDateTime);
}