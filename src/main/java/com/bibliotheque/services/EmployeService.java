package com.bibliotheque.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bibliotheque.entities.Employe;
import com.bibliotheque.repositories.EmployeRepository;

import java.util.List;
@Service
public class EmployeService {
    @Autowired
    private EmployeRepository employeRepository;

    public List<Employe> getAllEmploye() {
        return employeRepository.findAll();
    }

    public Employe getEmployeById(Long id) {
        return employeRepository.findById(id).orElse(null);
    }

    public Employe updateEmploye(Long id, Employe employe) {
        Employe existingEmploye = getEmployeById(id);
        existingEmploye.setNomEmp(employe.getNomEmp());
        existingEmploye.setNumEmp(employe.getNumEmp());
        existingEmploye.setBirth(employe.getBirth());
        existingEmploye.setDepartment(employe.getDepartment());
        return employeRepository.save(existingEmploye);
    }
}
