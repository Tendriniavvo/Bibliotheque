package com.bibliotheque.services;

import com.bibliotheque.repositories.BibliothecaireRepository;
import com.bibliotheque.entities.*;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.bibliotheque.entities.Bibliothecaire;

@Service
public class BibliothecaireService {
    public BibliothecaireRepository repository;

    public BibliothecaireService(BibliothecaireRepository repository) {
        this.repository = repository;
    }

    public Bibliothecaire save(Bibliothecaire biblio) {
        return repository.save(biblio);
    }

    public Optional<Bibliothecaire> findByUtilisateurId(Integer idUtilisateur) {
        return repository.findByIdUtilisateur(idUtilisateur);
    }
    
}
