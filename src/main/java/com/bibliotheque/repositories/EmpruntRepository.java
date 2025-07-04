package com.bibliotheque.repositories;

import com.bibliotheque.entities.Emprunt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.List;

@Repository
public interface EmpruntRepository extends JpaRepository<Emprunt, Integer> {

    @Query("SELECT COUNT(e) FROM Emprunt e " +
           "WHERE e.adherent.id = :idAdherent " +
           "AND e.dateEmprunt <= :date " +
           "AND e.dateRetourPrevue >= :date")
    long countByIdAdherentAndStatutEnCours(@Param("idAdherent") Integer idAdherent, 
                                           @Param("date") Instant date);

    Optional<Emprunt> findById(Integer id);

    List<Emprunt> findByExemplaireId(Integer exemplaireId);

}