package com.bibliotheque.services;

import com.bibliotheque.repositories.BibliothecaireRepository;
import com.bibliotheque.entities.*;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class BibliothecaireService {
    public BibliothecaireRepository repository;

    public BibliothecaireService(BibliothecaireRepository repository) {
        this.repository = repository;
    }
}
