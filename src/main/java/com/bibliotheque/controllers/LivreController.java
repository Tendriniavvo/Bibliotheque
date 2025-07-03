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
@RequestMapping("/livre")
public class LivreController {

    @Autowired
    private LivreService livreService;

    @Autowired
    private EditeurService editeurService;

    @Autowired
    private AdherentService adherentService;

    @GetMapping("/liste")
    public ModelAndView listeAAuteur() {
        List<Livre> livres = livreService.getAll();
        ModelAndView mv = new ModelAndView("bibliothecaire/template");
        mv.addObject("livres", livres);
        mv.addObject("contentPage", "livreListe.jsp");
        return mv;
    }

    @GetMapping("/form")
    public ModelAndView formAuteur() {
        List<Editeur> editeurs = editeurService.getAll();
        ModelAndView mv = new ModelAndView("bibliothecaire/template");
        mv.addObject("editeurs", editeurs);
        mv.addObject("contentPage", "livreForm.jsp");
        return mv;
    }

    @PostMapping("/save")
    public String saveLivre(
            @RequestParam("titre") String titre,
            @RequestParam(value = "isbn", required = false) String isbn,
            @RequestParam(value = "anneePublication", required = false) Integer anneePublication,
            @RequestParam(value = "resume", required = false) String resume,
            @RequestParam("idEditeur") Integer idEditeur) {

        Optional<Editeur> editeurOpt = editeurService.findById(idEditeur);
        if (editeurOpt.isEmpty()) {
            return "redirect:/livre/form?error=editeurNotFound";
        }

        Livre livre = new Livre();
        livre.setTitre(titre);
        livre.setIsbn(isbn);
        livre.setAnneePublication(anneePublication);
        livre.setResume(resume);
        livre.setEditeur(editeurOpt.get());

        livreService.save(livre);

        return "redirect:/livre/liste";
    }

}
