package com.bibliotheque.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import com.bibliotheque.entities.StatutEmprunt;
import java.util.Optional;

public interface StatutEmpruntRepository extends JpaRepository<StatutEmprunt, Integer> {
    // Optional<StatutEmprunt> findById(Integer id);
    // Optional<StatutEmprunt> findByLibelle(String libelle);
    // List<StatutEmprunt> findAll();
    // StatutEmprunt save(StatutEmprunt statutEmprunt);
    // void deleteById(Integer id);
    
}
