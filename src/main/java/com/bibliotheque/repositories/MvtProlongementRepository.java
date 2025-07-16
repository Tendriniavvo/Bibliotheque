package com.bibliotheque.repositories;

import com.bibliotheque.entities.MvtProlongement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MvtProlongementRepository extends JpaRepository<MvtProlongement, Integer> {
    List<MvtProlongement> findByProlongementIdOrderByDateMouvementDesc(Integer prolongementId);
} 