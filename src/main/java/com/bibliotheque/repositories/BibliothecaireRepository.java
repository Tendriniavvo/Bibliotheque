package com.bibliotheque.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bibliotheque.entities.Bibliothecaire;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BibliothecaireRepository extends JpaRepository<Bibliothecaire, Integer> {

    Optional<Bibliothecaire> findByIdUtilisateur(Integer idUtilisateur);

}