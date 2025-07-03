package com.bibliotheque.services;

import com.bibliotheque.repositories.PenaliteRepository;
import com.bibliotheque.entities.*;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class PenaliteService {
    public PenaliteRepository repository;

    public EmpruntService empruntService;

    public PenaliteService(PenaliteRepository repository, EmpruntService empruntService) {
        this.repository = repository;
        this.empruntService = empruntService;
    }

    public Penalite save(Penalite penalite){
        return repository.save(penalite);
    }

    public List<Penalite> getAll(){
        return repository.findAll();
    }

    public Optional<Emprunt> findEmpruntById(Integer id){
        return empruntService.findById(id);
    }


}
