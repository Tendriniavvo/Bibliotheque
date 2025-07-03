package com.bibliotheque.services;

import com.bibliotheque.repositories.EditeurRepository;
import com.bibliotheque.entities.*;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class EditeurService {
    public EditeurRepository repository;
    
    public EditeurService(EditeurRepository repository) {
        this.repository = repository;
    }

    public List<Editeur> getAll() {
        return repository.findAll();
    }

    public Editeur save(Editeur editeur) {
        return repository.save(editeur);
    }

    public Optional<Editeur> findById(Integer id) {
        return repository.findById(id);
    }
}
