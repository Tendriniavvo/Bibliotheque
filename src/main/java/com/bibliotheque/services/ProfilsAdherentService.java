package com.bibliotheque.services;

import com.bibliotheque.repositories.ProfilsAdherentRepository;
import com.bibliotheque.entities.*;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ProfilsAdherentService {
    public ProfilsAdherentRepository repository;

    public ProfilsAdherentService(ProfilsAdherentRepository repository) {
        this.repository = repository;
    }

    public List<ProfilsAdherent> getAll(){
        return repository.findAll();
    }
}
