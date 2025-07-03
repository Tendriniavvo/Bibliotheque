package com.bibliotheque.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import com.bibliotheque.entities.StatutEmprunt;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatutEmpruntRepository extends JpaRepository<StatutEmprunt, Integer> {
    // Optional<StatutEmprunt> findById(Integer id);
    // Optional<StatutEmprunt> findByLibelle(String libelle);
    // List<StatutEmprunt> findAll();
    // StatutEmprunt save(StatutEmprunt statutEmprunt);
    // void deleteById(Integer id);
    
}
