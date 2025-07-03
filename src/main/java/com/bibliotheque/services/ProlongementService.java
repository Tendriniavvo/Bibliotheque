package com.bibliotheque.services;

import com.bibliotheque.repositories.ProlongementRepository;
import com.bibliotheque.entities.*;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ProlongementService {

    public ProlongementRepository repository;

    public ProlongementService(ProlongementRepository repository) {
        this.repository = repository;
    }

    public List<Prolongement> getAll() {
        return repository.findAll();
    }

    public Prolongement save(Prolongement prolongement) {
        return repository.save(prolongement);
    }

    public Optional<Prolongement> findById(Integer id) {
        return repository.findById(id);
    }

    
}
