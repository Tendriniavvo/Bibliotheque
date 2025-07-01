package com.film.services;


import com.film.entities.Employe;
import com.film.repositories.EmployeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
