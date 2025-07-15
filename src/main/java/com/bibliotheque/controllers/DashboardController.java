package com.bibliotheque.controllers;

import com.bibliotheque.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    @Autowired
    private AdherentService adherentService;
    @Autowired
    private LivreService livreService;
    @Autowired
    private ExemplaireService exemplaireService;
    @Autowired
    private EmpruntService empruntService;
    @Autowired
    private PenaliteService penaliteService;
    @Autowired
    private AbonnementService abonnementService;

    @GetMapping("")
    public ModelAndView dashboard() {
        ModelAndView mv = new ModelAndView("bibliothecaire/template");
        mv.addObject("nbAdherents", adherentService.getAll().size());
        mv.addObject("nbLivres", livreService.getAll().size());
        mv.addObject("nbExemplaires", exemplaireService.getAll().size());
        mv.addObject("nbEmpruntsEnCours", empruntService.getEmpruntsEnCours(null).size());
        mv.addObject("nbPenalitesActives", penaliteService.getAll().size());
        mv.addObject("nbAbonnementsActifs", abonnementService.getAll().size());
        mv.addObject("contentPage", "dashboard.jsp");
        return mv;
    }
} 