package com.bibliotheque.repositories;

import com.bibliotheque.entities.StatutProlongement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatutProlongementRepository extends JpaRepository<StatutProlongement, Integer> {
    Optional<StatutProlongement> findByCodeStatut(String codeStatut);
} 