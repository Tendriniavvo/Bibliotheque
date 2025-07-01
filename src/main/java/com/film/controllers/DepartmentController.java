package com.film.controllers;



import com.film.entities.Department;
import com.film.services.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/departments")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    @GetMapping("")
    public ModelAndView listDepartments(){
        ModelAndView mv = new ModelAndView("dept/listeDept");
        List<Department> departments = departmentService.getAllDepartments();
        mv.addObject("departments", departments);
        return mv;
    }
}
