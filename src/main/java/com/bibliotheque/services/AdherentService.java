package com.bibliotheque.services;

import com.bibliotheque.repositories.AdherentRepository;
import com.bibliotheque.entities.*;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class AdherentService {
    public AdherentRepository repository;

    public AdherentService(AdherentRepository repository) {
        this.repository = repository;
    }

    public List<Adherent> getAbonnement(){
        return repository.findAll();
    }

    public Adherent saveAdherent(Adherent adherent) {
        return repository.save(adherent);
    }
}
