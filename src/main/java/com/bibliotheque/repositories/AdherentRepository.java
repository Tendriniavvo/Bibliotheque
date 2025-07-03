package com.bibliotheque.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bibliotheque.entities.Adherent;
import com.bibliotheque.entities.Bibliothecaire;

import java.util.Optional;


public interface AdherentRepository extends JpaRepository<Adherent, Integer> {

    Optional<Adherent> findByIdUtilisateur(Integer idUtilisateur);
}