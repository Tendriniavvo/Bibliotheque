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
import java.util.*;

@Controller
@RequestMapping("/auteur")
public class AuteurController {

    @Autowired
    private AuteurService auteurService;

    @Autowired
    private AdherentService adherentService;

    @GetMapping("/liste")
    public ModelAndView listeAAuteur() {
        List<Auteur> auteurs = auteurService.getAll();
        ModelAndView mv = new ModelAndView("bibliothecaire/template");
        mv.addObject("auteurs", auteurs);
        mv.addObject("contentPage", "auteurListe.jsp");
        return mv;
    }

    @GetMapping("/form")
    public ModelAndView formAuteur() {
        ModelAndView mv = new ModelAndView("bibliothecaire/template");
        mv.addObject("contentPage", "auteurForm.jsp");
        return mv;
    }

    @PostMapping("/save")
    public String saveAuteur(
            @RequestParam("nom") String nom,
            @RequestParam(value = "prenom", required = false) String prenom) {

        Auteur auteur = new Auteur();
        auteur.setNom(nom);
        auteur.setPrenom(prenom);

        auteurService.save(auteur); // ou repository.save(auteur) selon ta structure

        return "redirect:/auteur/liste";
    }

}
