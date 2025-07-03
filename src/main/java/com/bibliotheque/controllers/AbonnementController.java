package com.bibliotheque.controllers;

import com.bibliotheque.entities.*;
import com.bibliotheque.models.RegisterForm;
import com.bibliotheque.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import java.util.*;

@Controller
@RequestMapping("/abonnement")
public class AbonnementController {

    @Autowired
    private AbonnementService abonnementService;

    @Autowired
    private AdherentService adherentService;


    @GetMapping("/liste")
    public ModelAndView listeAbonnement(){
        List<Abonnement> abonnements = abonnementService.getAll();
        ModelAndView mv = new ModelAndView("bibliothecaire/template");
        mv.addObject("abonnements", abonnements);
        mv.addObject("contentPage", "abonnementListe.jsp");
        return mv;
    }

    @GetMapping("/form")
    public ModelAndView formAbonnement(){
        List<Adherent> adherents = adherentService.getAll();
        ModelAndView mv = new ModelAndView("bibliothecaire/template");
        mv.addObject("adherents",adherents);
        mv.addObject("contentPage", "abonnementForm.jsp");
        return mv;
    }

    @PostMapping("/save")
    public ModelAndView save()
}
