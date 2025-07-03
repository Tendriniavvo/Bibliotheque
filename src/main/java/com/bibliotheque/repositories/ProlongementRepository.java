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
}
