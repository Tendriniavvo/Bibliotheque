package com.bibliotheque.repositories;

import com.bibliotheque.entities.Penalite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PenaliteRepository extends JpaRepository<Penalite, Integer> {

    @Query("SELECT p FROM Penalite p " +
           "WHERE p.idAdherent = :idAdherent " +
           "AND (p.dateFin IS NULL OR p.dateFin >= :currentDate)")
    List<Penalite> findActivePenalitesByAdherent(@Param("idAdherent") Integer idAdherent,
                                                @Param("currentDate") LocalDate currentDate);
}