package com.bibliotheque.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bibliotheque.entities.Editeur;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EditeurRepository extends JpaRepository<Editeur, Integer> {

    public Optional<Editeur> findById(Integer id);
    
    // Other methods can be added as needed, such as custom queries
    // List<Editeur> findAll();
    // Editeur save(Editeur editeur);
    // void deleteById(Integer id);
    // void delete(Editeur editeur);
    
}
