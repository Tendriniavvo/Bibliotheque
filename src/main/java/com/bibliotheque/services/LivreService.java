package com.bibliotheque.services;

import com.bibliotheque.repositories.LivreRepository;
import com.bibliotheque.entities.*;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class LivreService {
    public LivreRepository repository;
    
    public LivreService(LivreRepository repository) {
        this.repository = repository;
    }
}
