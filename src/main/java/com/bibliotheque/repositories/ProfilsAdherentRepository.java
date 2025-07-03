package com.bibliotheque.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bibliotheque.entities.ProfilsAdherent;
import java.util.Optional;


public interface ProfilsAdherentRepository extends JpaRepository<ProfilsAdherent, Integer> {
}