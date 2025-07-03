package com.bibliotheque.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import com.bibliotheque.entities.Emprunt;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpruntRepository extends JpaRepository<Emprunt, Integer> {
    // Optional<Emprunt> findById(Integer id);
    // List<Emprunt> findAll();
    // Emprunt save(Emprunt emprunt);
    // void deleteById(Integer id);
    // void delete(Emprunt emprunt);
    
}
