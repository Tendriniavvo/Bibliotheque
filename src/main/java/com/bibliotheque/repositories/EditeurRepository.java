package com.bibliotheque.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bibliotheque.entities.Editeur;
import java.util.Optional;

public interface EditeurRepository extends JpaRepository<Editeur, Integer> {

    
}
