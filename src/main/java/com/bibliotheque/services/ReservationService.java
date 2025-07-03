package com.bibliotheque.services;

import com.bibliotheque.repositories.ReservationRepository;
import com.bibliotheque.entities.*;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {
    public ReservationRepository repository;

    public ReservationService(ReservationRepository repository) {
        this.repository = repository;
    }

    public List<Reservation> getAll() {
        return repository.findAll();
    }

    public Reservation save(Reservation reservation) {
        return repository.save(reservation);
    }

    public Optional<Reservation> findById(Integer id) {
        return repository.findById(id);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
    
}
