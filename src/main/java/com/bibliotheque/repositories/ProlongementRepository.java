package com.bibliotheque.repositories;

import com.bibliotheque.entities.Prolongement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import java.util.List;

@Repository
public interface ProlongementRepository extends JpaRepository<Prolongement, Integer> {

    // Exemple : récupérer tous les prolongements d'un emprunt
    List<Prolongement> findByEmpruntId(Integer empruntId);

    public Optional<Prolongement> findById(Integer id);

    long countByEmpruntId(Integer empruntId);

    @org.springframework.data.jpa.repository.Query(value = """
        SELECT COUNT(*) FROM prolongements p
        WHERE p.id_emprunt = :empruntId
          AND (
            SELECT sp.code_statut
            FROM mvt_prolongement m
            JOIN statuts_prolongement sp ON m.id_statut_nouveau = sp.id_statut
            WHERE m.id_prolongement = p.id_prolongement
            ORDER BY m.date_mouvement DESC
            LIMIT 1
          ) = 'En attente'
        """, nativeQuery = true)
    long countActiveProlongementsByEmpruntId(@org.springframework.data.repository.query.Param("empruntId") Integer empruntId);
}
