package com.bibliotheque.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bibliotheque.entities.Bibliothecaire;
import java.util.Optional;


public interface BibliothecaireRepository extends JpaRepository<Bibliothecaire, Integer> {

    Optional<Bibliothecaire> findByIdUtilisateur(Integer idUtilisateur);

}