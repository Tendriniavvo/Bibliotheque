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
@RequestMapping("/abonnement")
public class AbonnementController {

    @Autowired
    private AbonnementService abonnementService;

    @Autowired
    private AdherentService adherentService;

    @GetMapping("/liste")
    public ModelAndView listeAbonnement() {
        List<Abonnement> abonnements = abonnementService.getAll();
        ModelAndView mv = new ModelAndView("bibliothecaire/template");
        mv.addObject("abonnements", abonnements);
        mv.addObject("contentPage", "abonnementListe.jsp");
        return mv;
    }

    @GetMapping("/form")
    public ModelAndView formAbonnement() {
        List<Adherent> adherents = adherentService.getAll();
        ModelAndView mv = new ModelAndView("bibliothecaire/template");
        mv.addObject("adherents", adherents);
        mv.addObject("contentPage", "abonnementForm.jsp");
        return mv;
    }

    @PostMapping("/save")
    public String saveAbonnement(
            @RequestParam("idAdherent") Integer idAdherent,
            @RequestParam("dateDebut") String dateDebut,
            @RequestParam("dateFin") String dateFin) {

        Abonnement abonnement = new Abonnement();

        Optional<Adherent> adherentOpt = adherentService.findById(idAdherent);
        if (adherentOpt.isEmpty()) {
            return "redirect:/abonnement/form?error=adherentNotFound";
        }

        abonnement.setAdherent(adherentOpt.get());
        abonnement.setDateDebut(LocalDate.parse(dateDebut));
        abonnement.setDateFin(LocalDate.parse(dateFin));

        abonnementService.save(abonnement);

        return "redirect:/abonnement/liste";
    }

}
