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
@RequestMapping("/editeur")
public class EditeurController {
    @Autowired
    private EditeurService editeurService;

    @Autowired
    private AdherentService adherentService;

    @GetMapping("/liste")
    public ModelAndView listeAAuteur() {
        List<Editeur> editeurs = editeurService.getAll();
        ModelAndView mv = new ModelAndView("bibliothecaire/template");
        mv.addObject("editeurs", editeurs);
        mv.addObject("contentPage", "editeurListe.jsp");
        return mv;
    }

    @GetMapping("/form")
    public ModelAndView formAuteur() {
        ModelAndView mv = new ModelAndView("bibliothecaire/template");
        mv.addObject("contentPage", "editeurForm.jsp");
        return mv;
    }

    @PostMapping("/save")
    public String saveEditeur(@RequestParam("nom") String nom) {
        Editeur editeur = new Editeur();
        editeur.setNom(nom);

        editeurService.save(editeur); 
        return "redirect:/editeur/liste";
    }

}
