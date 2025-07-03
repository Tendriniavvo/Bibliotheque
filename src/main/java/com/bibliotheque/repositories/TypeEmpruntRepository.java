package com.bibliotheque.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import com.bibliotheque.entities.TypeEmprunt;
import java.util.Optional;

public interface TypeEmpruntRepository extends JpaRepository<TypeEmprunt, Integer> {
    // Optional<TypeEmprunt> findById(Integer id);
    // List<TypeEmprunt> findAll();
    // TypeEmprunt save(TypeEmprunt typeEmprunt);
    // void deleteById(Integer id);
    // boolean existsById(Integer id);
    
}
