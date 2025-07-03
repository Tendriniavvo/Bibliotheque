package com.bibliotheque.repositories;

import com.bibliotheque.entities.JourFerie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface JourFerieRepository extends JpaRepository<JourFerie, LocalDate> {

    // Vérifie si une date est un jour férié
    boolean existsByDateFerie(LocalDate dateFerie);

}
