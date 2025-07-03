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
}
