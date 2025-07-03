package com.bibliotheque.services;

import com.bibliotheque.repositories.StatutEmpruntRepository;
import com.bibliotheque.entities.*;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class StatutEmpruntService {
    public StatutEmpruntRepository repository;

    
    public StatutEmpruntService(StatutEmpruntRepository repository) {
        this.repository = repository;
    }
}
