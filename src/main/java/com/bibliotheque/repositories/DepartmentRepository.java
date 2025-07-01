package com.bibliotheque.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bibliotheque.entities.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
  }