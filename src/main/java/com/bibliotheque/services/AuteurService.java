package com.bibliotheque.services;

import com.bibliotheque.repositories.AuteurRepository;
import com.bibliotheque.entities.*;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class AuteurService {
    public AuteurRepository repository;

    public AuteurService(AuteurRepository repository) {
        this.repository = repository;
    }

    public List<Auteur> getAll() {
        return repository.findAll();
    }

    public Auteur save(Auteur auteur) {
        return repository.save(auteur);
    }   

}
