package com.bibliotheque.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bibliotheque.entities.Utilisateur;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {

    Optional<Utilisateur> findByEmail(String email);
}