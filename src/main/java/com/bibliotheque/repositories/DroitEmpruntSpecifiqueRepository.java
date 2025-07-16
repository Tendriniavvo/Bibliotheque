package com.bibliotheque.repositories;

import com.bibliotheque.entities.DroitEmpruntSpecifique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DroitEmpruntSpecifiqueRepository extends JpaRepository<DroitEmpruntSpecifique, Integer> {

    List<DroitEmpruntSpecifique> findByLivreId(Integer livreId);

    List<DroitEmpruntSpecifique> findByProfilId(Integer profilId);

    Optional<DroitEmpruntSpecifique> findByLivreIdAndProfilId(Integer livreId, Integer profilId);
}