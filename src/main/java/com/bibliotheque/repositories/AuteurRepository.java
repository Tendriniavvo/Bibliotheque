package com.bibliotheque.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bibliotheque.entities.Auteur;
import java.util.Optional;

public interface AuteurRepository extends JpaRepository<Auteur, Integer> {

    
}
