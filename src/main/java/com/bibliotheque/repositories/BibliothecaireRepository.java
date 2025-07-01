package com.bibliotheque.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bibliotheque.entities.Bibliothecaire;


public interface BibliothecaireRepository extends JpaRepository<Bibliothecaire, Integer> {
}