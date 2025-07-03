package com.bibliotheque.controllers;

import com.bibliotheque.entities.*;
import com.bibliotheque.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/emprunt")
public class EmpruntController {

    @Autowired
    private EmpruntService empruntService;

    @Autowired
    private AdherentService adherentService;

    @Autowired
    private ExemplaireService exemplaireService;

    @Autowired
    private TypeEmpruntService typeEmpruntService;

    @GetMapping("/liste")
    public ModelAndView listeEmprunts() {
        List<Emprunt> emprunts = empruntService.getAll();
        ModelAndView mv = new ModelAndView("bibliothecaire/template");
        mv.addObject("emprunts", emprunts);
        mv.addObject("contentPage", "empruntListe.jsp");
        return mv;
    }

    @GetMapping("/form")
    public ModelAndView formEmprunt() {
        List<Adherent> adherents = adherentService.getAll();
        List<Exemplaire> exemplaires = exemplaireService.getAll();
        List<TypeEmprunt> typesEmprunt = typeEmpruntService.getAll();

        ModelAndView mv = new ModelAndView("bibliothecaire/template");
        mv.addObject("adherents", adherents);
        mv.addObject("exemplaires", exemplaires);
        mv.addObject("typesEmprunt", typesEmprunt);
        mv.addObject("contentPage", "empruntForm.jsp");
        return mv;
    }

    @PostMapping("/save")
    public String saveEmprunt(
            @RequestParam("idAdherent") Integer idAdherent,
            @RequestParam("idExemplaire") Integer idExemplaire,
            @RequestParam("idTypeEmprunt") Integer idTypeEmprunt,
            @RequestParam("dateEmprunt") String dateEmpruntStr,
            @RequestParam("dateRetourPrevue") String dateRetourPrevueStr) {

        Optional<Adherent> adherentOpt = adherentService.findById(idAdherent);
        if (adherentOpt.isEmpty()) {
            return "redirect:/emprunt/form?error=adherentNotFound";
        }

        Optional<Exemplaire> exemplaireOpt = exemplaireService.findById(idExemplaire);
        if (exemplaireOpt.isEmpty()) {
            return "redirect:/emprunt/form?error=exemplaireNotFound";
        }

        Optional<TypeEmprunt> typeEmpruntOpt = typeEmpruntService.findById(idTypeEmprunt);
        if (typeEmpruntOpt.isEmpty()) {
            return "redirect:/emprunt/form?error=typeEmpruntNotFound";
        }

        Emprunt emprunt = new Emprunt();
        emprunt.setAdherent(adherentOpt.get());
        emprunt.setExemplaire(exemplaireOpt.get());
        emprunt.setTypeEmprunt(typeEmpruntOpt.get());

        // Parse dates from String (format attendu : yyyy-MM-dd'T'HH:mm, exemple: 2025-07-03T20:30)
        Instant dateEmprunt = LocalDateTime.parse(dateEmpruntStr).atZone(ZoneId.systemDefault()).toInstant();
        Instant dateRetourPrevue = LocalDateTime.parse(dateRetourPrevueStr).atZone(ZoneId.systemDefault()).toInstant();

        emprunt.setDateEmprunt(dateEmprunt);
        emprunt.setDateRetourPrevue(dateRetourPrevue);

        empruntService.save(emprunt);

        return "redirect:/emprunt/liste";
    }
}
