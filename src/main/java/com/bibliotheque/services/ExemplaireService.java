package com.bibliotheque.services;

import com.bibliotheque.repositories.ExemplaireRepository;
import com.bibliotheque.entities.*;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ExemplaireService {
    public ExemplaireRepository repository;
    
    public ExemplaireService(ExemplaireRepository repository) {
        this.repository = repository;
    }
}
