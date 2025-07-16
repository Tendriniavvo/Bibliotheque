package com.bibliotheque.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bibliotheque.entities.ProfilsAdherent;
import java.util.Optional;

@Repository
public interface ProfilsAdherentRepository extends JpaRepository<ProfilsAdherent, Integer> {
}