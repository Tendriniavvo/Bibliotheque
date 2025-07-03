package com.bibliotheque.controllers;

import com.bibliotheque.entities.Emprunt;
import com.bibliotheque.entities.Prolongement;
import com.bibliotheque.services.EmpruntService;
import com.bibliotheque.services.ProlongementService;
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
@RequestMapping("/prolongement")
public class ProlongementController {

    @Autowired
    private ProlongementService prolongementService;

    @Autowired
    private EmpruntService empruntService;

    @GetMapping("/liste")
    public ModelAndView listeProlongements() {
        List<Prolongement> prolongements = prolongementService.getAll();
        ModelAndView mv = new ModelAndView("bibliothecaire/template");
        mv.addObject("prolongements", prolongements);
        mv.addObject("contentPage", "prolongementListe.jsp");
        return mv;
    }

    @GetMapping("/form")
    public ModelAndView formProlongement() {
        List<Emprunt> emprunts = empruntService.getAll();
        ModelAndView mv = new ModelAndView("bibliothecaire/template");
        mv.addObject("emprunts", emprunts);
        mv.addObject("contentPage", "prolongementForm.jsp");
        return mv;
    }

    @PostMapping("/save")
    public String saveProlongement(
            @RequestParam("idEmprunt") Integer idEmprunt,
            @RequestParam("dateFin") String dateFinStr,
            @RequestParam("dateProlongement") String dateProlongementStr
    ) {
        Optional<Emprunt> empruntOpt = empruntService.findById(idEmprunt);
        if (empruntOpt.isEmpty()) {
            return "redirect:/prolongement/form?error=empruntNotFound";
        }

        // Convertir les dates au format Instant
        Instant dateFin = LocalDateTime.parse(dateFinStr).atZone(ZoneId.systemDefault()).toInstant();
        Instant dateProlongement = LocalDateTime.parse(dateProlongementStr).atZone(ZoneId.systemDefault()).toInstant();

        Prolongement prolongement = new Prolongement();
        prolongement.setEmprunt(empruntOpt.get());
        prolongement.setDateFin(dateFin);
        prolongement.setDateProlongement(dateProlongement);

        prolongementService.save(prolongement);

        return "redirect:/prolongement/liste";
    }
}
