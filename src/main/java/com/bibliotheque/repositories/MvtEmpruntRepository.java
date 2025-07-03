package com.bibliotheque.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import com.bibliotheque.entities.MvtEmprunt;
import java.util.Optional;

public interface MvtEmpruntRepository extends JpaRepository<MvtEmprunt, Integer> {
    // Optional<MvtEmprunt> findById(Integer id);
    // List<MvtEmprunt> findAll();
    // MvtEmprunt save(MvtEmprunt mvtEmprunt);
    // void deleteById(Integer id);
    // void delete(MvtEmprunt mvtEmprunt);
    
}
