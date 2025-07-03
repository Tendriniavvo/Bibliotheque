package com.bibliotheque.controllers;

import com.bibliotheque.entities.ProfilsAdherent;
import com.bibliotheque.services.ProfilsAdherentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ProfilsAdherentService profilsAdherentService;

    @GetMapping("/")
    public String home() {
        return "auth/login"; 
    }

    @GetMapping("/signup")
    public ModelAndView register() {
        List<ProfilsAdherent> profils = profilsAdherentService.getAll();
        ModelAndView mv = new ModelAndView("auth/signup");
        mv.addObject("profils", profils); 
        return mv;
    }
}
