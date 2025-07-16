package com.bibliotheque.services;

import com.bibliotheque.repositories.StatutReservationRepository;
import com.bibliotheque.entities.*;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class StatutReservationService {
    public StatutReservationRepository repository;
    public StatutReservationService(StatutReservationRepository repository) {
        this.repository = repository;
    }

    public List<StatutReservation> getAll() {
        return repository.findAll();
    }

    public Optional<StatutReservation> findById(Integer id) {
        return repository.findById(id);
    }
    
}
