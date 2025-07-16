package com.bibliotheque.controllers;

import com.bibliotheque.services.AdherentService;
import com.bibliotheque.entities.Adherent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;

@Controller
@RequestMapping("/adherent")
public class AdherentController {
    @Autowired
    private AdherentService adherentService;

    @GetMapping("/liste")
    public ModelAndView listeAdherents() {
        List<Adherent> adherents = adherentService.getAll();
        ModelAndView mv = new ModelAndView("bibliothecaire/template");
        mv.addObject("adherents", adherents);
        mv.addObject("contentPage", "adherentListe.jsp");
        return mv;
    }
} 