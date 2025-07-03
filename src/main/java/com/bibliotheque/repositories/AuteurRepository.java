package com.bibliotheque.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bibliotheque.entities.Auteur;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuteurRepository extends JpaRepository<Auteur, Integer> {

    
}
