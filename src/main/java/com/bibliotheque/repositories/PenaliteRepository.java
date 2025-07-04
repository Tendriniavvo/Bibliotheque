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

       @Query(value = """
                     SELECT * FROM penalites
                     WHERE id_adherent = :idAdherent
                     AND date_debut <= :currentDate
                     AND date_debut + INTERVAL '1 DAY' * jour >= :currentDate
                     """, nativeQuery = true)
       List<Penalite> findActivePenalitesByAdherent(
                     @Param("idAdherent") Integer idAdherent,
                     @Param("currentDate") LocalDate currentDate);

       public List<Penalite> findByAdherentId(Integer adherentId);
}
