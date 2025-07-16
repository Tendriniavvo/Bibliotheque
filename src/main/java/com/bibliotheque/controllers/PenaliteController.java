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
@RequestMapping("/penalite")
public class PenaliteController {

    @Autowired
    private AdherentService adherentService;

    @Autowired
    private EmpruntService empruntService;

    @Autowired
    private PenaliteService penaliteService;

    @Autowired
    private ProfilsAdherentService profilsAdherentService;

    @GetMapping("/liste")
    public ModelAndView listePenalite() {
        List<Penalite> penalites = penaliteService.getAll();
        ModelAndView mv = new ModelAndView("bibliothecaire/template");
        mv.addObject("penalites", penalites);
        mv.addObject("contentPage", "penaliteListe.jsp");
        return mv;
    }

    @GetMapping("/form")
    public ModelAndView formPenalite() {
        List<Adherent> adherents = adherentService.getAll();
        List<Emprunt> emprunts = empruntService.findAll();
        ModelAndView mv = new ModelAndView("bibliothecaire/template");
        mv.addObject("adherents", adherents);
        mv.addObject("emprunts", emprunts);
        mv.addObject("contentPage", "penaliteForm.jsp");
        return mv;
    }

    @PostMapping("/save")
    public String save(
            @RequestParam("idEmprunt") Integer idEmprunt,
            @RequestParam("idAdherent") Integer idAdherent,
            @RequestParam("dateDebut") String dateDebut,
            //@RequestParam("jour") Integer jour, // ignoré, remplacé par le quota du profil
            @RequestParam(value = "raison", required = false) String raison) {

        Optional<Adherent> adherentOpt = adherentService.findById(idAdherent);
        if (adherentOpt.isEmpty()) {
            return "redirect:/penalite/form?error=adherentNotFound";
        }

        Optional<Emprunt> empruntOpt = penaliteService.findEmpruntById(idEmprunt);
        if (empruntOpt.isEmpty()) {
            return "redirect:/penalite/form?error=empruntNotFound";
        }

        // Récupérer le quota joursPenalite du profil de l'adhérent
        Adherent adherent = adherentOpt.get();
        Integer joursPenalite = null;
        if (adherent.getIdProfil() != null) {
            Optional<ProfilsAdherent> profilOpt = profilsAdherentService.repository.findById(adherent.getIdProfil());
            if (profilOpt.isPresent()) {
                joursPenalite = profilOpt.get().getJoursPenalite();
            }
        }
        if (joursPenalite == null) {
            joursPenalite = 1; // Valeur par défaut si non trouvée
        }

        Penalite penalite = new Penalite();
        penalite.setAdherent(adherentOpt.get());
        penalite.setEmprunt(empruntOpt.get());
        penalite.setDateDebut(LocalDate.parse(dateDebut));
        penalite.setJour(joursPenalite);
        penalite.setRaison(raison);

        penaliteService.save(penalite);

        return "redirect:/penalite/liste";
    }

}
