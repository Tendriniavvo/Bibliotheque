package com.bibliotheque.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bibliotheque.entities.Abonnement;
import java.util.Optional;
public interface AbonnementRepository extends JpaRepository<Abonnement, Integer> {
}