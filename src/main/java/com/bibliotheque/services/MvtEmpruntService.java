package com.bibliotheque.services;

import com.bibliotheque.repositories.MvtEmpruntRepository;
import com.bibliotheque.entities.*;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class MvtEmpruntService {
    public MvtEmpruntRepository repository;
    
    public MvtEmpruntService(MvtEmpruntRepository repository) {
        this.repository = repository;
    }
}
