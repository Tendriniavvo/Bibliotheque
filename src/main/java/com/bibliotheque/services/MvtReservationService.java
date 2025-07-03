package com.bibliotheque.services;

import com.bibliotheque.repositories.MvtReservationRepository;
import com.bibliotheque.entities.*;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class MvtReservationService {
    public MvtReservationRepository repository;
    public MvtReservationService(MvtReservationRepository repository) {
        this.repository = repository;
    }
}
