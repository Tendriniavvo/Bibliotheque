package com.bibliotheque.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bibliotheque.entities.Employe;

public interface EmployeRepository extends JpaRepository<Employe, Long> {
  }