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
@RequestMapping("/exemplaire")
public class ExemplaireController {

    @Autowired
    private LivreService livreService;

    @Autowired
    private ExemplaireService exemplaireService;

    @Autowired
    private AdherentService adherentService;

    @GetMapping("/liste")
    public ModelAndView listeAAuteur() {
        List<Exemplaire> exemplaires = exemplaireService.getAll();
        ModelAndView mv = new ModelAndView("bibliothecaire/template");
        mv.addObject("exemplaires", exemplaires);
        mv.addObject("contentPage", "exemplaireListe.jsp");
        return mv;
    }

    @GetMapping("/form")
    public ModelAndView formAuteur() {
        List<Livre> livres = livreService.getAll();
        ModelAndView mv = new ModelAndView("bibliothecaire/template");
        mv.addObject("livres", livres);
        mv.addObject("contentPage", "exemplaireForm.jsp");
        return mv;
    }

    @PostMapping("/save")
    public String saveExemplaire(
            @RequestParam("idLivre") Integer idLivre,
            @RequestParam("quantite") Integer quantite) {

        Optional<Livre> livreOpt = livreService.findById(idLivre);
        if (livreOpt.isEmpty()) {
            return "redirect:/exemplaire/form?error=livreNotFound";
        }

        Exemplaire exemplaire = new Exemplaire();
        exemplaire.setLivre(livreOpt.get());
        exemplaire.setQuantite(quantite);

        exemplaireService.save(exemplaire);

        return "redirect:/exemplaire/liste";
    }

}
