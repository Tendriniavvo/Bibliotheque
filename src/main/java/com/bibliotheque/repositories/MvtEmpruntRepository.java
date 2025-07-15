package com.bibliotheque.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bibliotheque.entities.MvtEmprunt;
import java.util.Optional;

@Repository
public interface MvtEmpruntRepository extends JpaRepository<MvtEmprunt, Integer> {
    // Optional<MvtEmprunt> findById(Integer id);
    // List<MvtEmprunt> findAll();
    // MvtEmprunt save(MvtEmprunt mvtEmprunt);
    // void deleteById(Integer id);
    // void delete(MvtEmprunt mvtEmprunt);

    public Optional<MvtEmprunt> findTopByEmpruntIdOrderByDateMouvementDesc(Integer empruntId);

    @Query("SELECT m FROM MvtEmprunt m WHERE m.emprunt.id = :empruntId ORDER BY m.dateMouvement DESC")
    Optional<MvtEmprunt> findDernierStatutByEmpruntId(@Param("empruntId") Integer empruntId);

}
