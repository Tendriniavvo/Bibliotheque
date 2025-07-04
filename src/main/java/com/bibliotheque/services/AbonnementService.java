package com.bibliotheque.services;

import com.bibliotheque.repositories.AbonnementRepository;
import com.bibliotheque.entities.*;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class AbonnementService {
    public AbonnementRepository repository;

    public AbonnementService(AbonnementRepository repository) {
        this.repository = repository;
    }

    public List<Abonnement> getAbonnement(){
        return repository.findAll();
    }
}
