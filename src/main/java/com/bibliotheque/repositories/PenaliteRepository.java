package com.bibliotheque.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bibliotheque.entities.Penalite;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PenaliteRepository extends JpaRepository<Penalite, Integer> {
   
}