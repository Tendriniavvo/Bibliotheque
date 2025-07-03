package com.bibliotheque.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import com.bibliotheque.entities.Livre;
import java.util.Optional;

public interface LivreRepository extends JpaRepository<Livre, Integer> {
    // Optional<Livre> findByTitre(String titre);
    // Optional<Livre> findByIsbn(String isbn);
    // Optional<Livre> findByAuteurNom(String auteurNom);
    // Optional<Livre> findByAuteurPrenom(String auteurPrenom);
    // Optional<Livre> findByGenreLibelle(String genreLibelle);
    
}
