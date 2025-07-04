package com.bibliotheque.repositories;

import com.bibliotheque.entities.Emprunt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

@Repository
public interface EmpruntRepository extends JpaRepository<Emprunt, Integer> {

    @Query("SELECT COUNT(e) FROM Emprunt e " +
            "WHERE e.adherent.id = :idAdherent " +
            "AND e.dateEmprunt <= :date " +
            "AND e.dateRetourPrevue >= :date")
    long countByIdAdherentAndStatutEnCours(@Param("idAdherent") Integer idAdherent,
            @Param("date") LocalDate date);

    Optional<Emprunt> findById(Integer id);

    List<Emprunt> findByExemplaireId(Integer exemplaireId);

    @Query("SELECT COUNT(e) FROM Emprunt e WHERE e.exemplaire.livre.id = :livreId " +
            "AND CAST(e.dateEmprunt AS date) <= :date " +
            "AND CAST(e.dateRetourPrevue AS date) >= :date " +
            "AND e.id IN (SELECT m.emprunt.id FROM MvtEmprunt m WHERE m.statutNouveau.codeStatut IN ('En cours', 'Retard'))")
    public long countActiveEmpruntsByLivreAndDate(@Param("livreId") Integer livreId, @Param("date") LocalDate date);

    @Query("SELECT COUNT(e) FROM Emprunt e WHERE e.adherent.id = :idAdherent " +
            "AND NOT EXISTS (SELECT 1 FROM MvtEmprunt m WHERE m.emprunt.id = e.id " +
            "AND m.statutNouveau.codeStatut IN ('RENDU', 'PERDU', 'Annulée'))")
    public long countByIdAdherentAndStatutEnCours(@Param("idAdherent") Integer idAdherent);
  


}