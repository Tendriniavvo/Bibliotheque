package com.bibliotheque.repositories;

import com.bibliotheque.entities.Abonnement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface AbonnementRepository extends JpaRepository<Abonnement, Integer> {

    @Query("SELECT a FROM Abonnement a " +
           "WHERE a.idAdherent = :idAdherent " +
           "AND a.dateDebut <= :currentDate " +
           "AND a.dateFin >= :currentDate")
    Optional<Abonnement> findActiveAbonnementByAdherent(@Param("idAdherent") Integer idAdherent, 
                                                       @Param("currentDate") LocalDate currentDate);
}