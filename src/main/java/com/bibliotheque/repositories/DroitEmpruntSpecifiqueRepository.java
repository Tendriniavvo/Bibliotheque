package com.bibliotheque.repositories;

import com.bibliotheque.entities.DroitEmpruntSpecifique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DroitEmpruntSpecifiqueRepository extends JpaRepository<DroitEmpruntSpecifique, Integer> {

    List<DroitEmpruntSpecifique> findByIdLivre(Integer idLivre);

    List<DroitEmpruntSpecifique> findByIdProfil(Integer idProfil);

    Optional<DroitEmpruntSpecifique> findByIdLivreAndIdProfil(Integer idLivre, Integer idProfil);
}