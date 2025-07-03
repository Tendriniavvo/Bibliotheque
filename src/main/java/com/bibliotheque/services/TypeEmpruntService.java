package com.bibliotheque.services;
import com.bibliotheque.repositories.TypeEmpruntRepository;
import com.bibliotheque.entities.*;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class TypeEmpruntService {
    public TypeEmpruntRepository repository;
    
    public TypeEmpruntService(TypeEmpruntRepository repository) {
        this.repository = repository;
    }   
}
