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

    public List<TypeEmprunt> getAll() {
        return repository.findAll();
    }

    public Optional<TypeEmprunt> findById(Integer id) {
        return repository.findById(id);
    }
}
