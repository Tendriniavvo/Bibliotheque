package com.bibliotheque.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bibliotheque.entities.Adherent;


public interface AdherentRepository extends JpaRepository<Adherent, Integer> {
}