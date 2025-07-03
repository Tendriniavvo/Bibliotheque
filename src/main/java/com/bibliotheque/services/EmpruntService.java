package com.bibliotheque.services;

import com.bibliotheque.repositories.EmpruntRepository;
import com.bibliotheque.entities.*;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class EmpruntService {
    public EmpruntRepository repository;
    
    public EmpruntService(EmpruntRepository repository) {
        this.repository = repository;
    }
}
