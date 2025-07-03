package com.bibliotheque.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import com.bibliotheque.entities.Exemplaire;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ExemplaireRepository extends JpaRepository<Exemplaire , Integer> {
    public Optional<Exemplaire> findById(Integer id);
    // List<Exemplaire> findAll();
    // Exemplaire save(Exemplaire exemplaire);
    // void deleteById(Integer id);
    // void delete(Exemplaire exemplaire);
    // boolean existsById(Integer id);
    
}
