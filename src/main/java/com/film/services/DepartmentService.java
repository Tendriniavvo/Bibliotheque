package com.film.services;


import com.film.entities.Department;
import com.film.repositories.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id).orElse(null);
    }

    public Department updateDepartment(Long id, Department department){
        Department existeDepartment = getDepartmentById(id);
        existeDepartment.setNom(department.getNom());
        return departmentRepository.save(existeDepartment);
    }

    public void deleteDepartment(Long id){
        departmentRepository.deleteById(id);
    }

    public Department createDepartment(Department department) {
        return departmentRepository.save(department);
    }
}
