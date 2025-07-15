package com.bibliotheque.controllers;

import com.bibliotheque.entities.Emprunt;
import com.bibliotheque.entities.Prolongement;
import com.bibliotheque.exceptions.EmpruntException;
import com.bibliotheque.services.EmpruntService;
import com.bibliotheque.services.ProlongementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

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
        List<Emprunt> emprunts = empruntService.findAll();
        ModelAndView mv = new ModelAndView("bibliothecaire/template");
        mv.addObject("emprunts", emprunts);
        mv.addObject("contentPage", "prolongementForm.jsp");
        return mv;
    }

    // @GetMapping("/form")
    // public ModelAndView formProlongement(@RequestParam("id") Integer id) {
    //     Emprunt emprunt = empruntService.findById(id)
    //             .orElseThrow(() -> new IllegalArgumentException("Emprunt non trouvé : " + id));
    //     ModelAndView mv = new ModelAndView("bibliothecaire/template");
    //     mv.addObject("emprunt", emprunt);
    //     mv.addObject("contentPage", "prolongementForm.jsp");
    //     return mv;
    // }

    // @PostMapping("/save")
    // public String saveProlongement(
    //         @RequestParam("idEmprunt") Integer idEmprunt,
    //         @RequestParam("dateFin") String dateFinStr,
    //         RedirectAttributes redirectAttributes) {

    //     // Validation des paramètres de base
    //     if (idEmprunt == null || dateFinStr == null || dateFinStr.isEmpty()) {
    //         redirectAttributes.addFlashAttribute("errorMessage",
    //                 "L'identifiant de l'emprunt ou la date de fin est manquant.");
    //         return "redirect:/prolongement/form?id=" + idEmprunt;
    //     }

    //     // Création de l'objet Prolongement
    //     Prolongement prolongement = new Prolongement();
    //     try {
    //         // Récupération de l'emprunt
    //         Emprunt emprunt = empruntService.findById(idEmprunt)
    //                 .orElseThrow(() -> new EmpruntException("L'emprunt spécifié n'existe pas."));

    //         // Conversion de la date
    //         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    //         LocalDate dateFin = LocalDate.parse(dateFinStr, formatter);

    //         // Configuration du prolongement
    //         prolongement.setEmprunt(emprunt);
    //         prolongement.setDateFin(dateFin);
    //         prolongement.setDateProlongement(LocalDate.now());

    //         // Appel du service pour enregistrer le prolongement
    //         prolongementService.save(prolongement);
    //         redirectAttributes.addFlashAttribute("successMessage", "Prolongement ajouté avec succès.");
    //     } catch (DateTimeParseException e) {
    //         redirectAttributes.addFlashAttribute("errorMessage",
    //                 "Format de date invalide. Utilisez le format yyyy-MM-dd'T'HH:mm (ex. 2025-07-03T20:30).");
    //         return "redirect:/prolongement/form?id=" + idEmprunt;
    //     } catch (EmpruntException e) {
    //         redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
    //         return "redirect:/prolongement/form?id=" + idEmprunt;
    //     }

    //     return "redirect:/prolongement/liste";
    // }

    @PostMapping("/save")
    public String saveProlongement(
            @RequestParam("idEmprunt") Integer idEmprunt,
            @RequestParam("dateProlongation") String dateProlongationStr,
            RedirectAttributes redirectAttributes) {

        // Validation des paramètres de base
        if (idEmprunt == null || dateProlongationStr == null || dateProlongationStr.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "L'identifiant de l'emprunt ou la date de prolongation est manquant.");
            return "redirect:/prolongement/form?id=" + idEmprunt;
        }

        // Création de l'objet Prolongement
        Prolongement prolongement = new Prolongement();
        try {
            // Récupération de l'emprunt
            Emprunt emprunt = empruntService.findById(idEmprunt)
                    .orElseThrow(() -> new EmpruntException("L'emprunt spécifié n'existe pas."));

            // Conversion de la date (datetime-local)
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDate dateFin = LocalDate.parse(dateProlongationStr, formatter);

            // Configuration du prolongement
            prolongement.setEmprunt(emprunt);
            prolongement.setDateFin(dateFin);
            prolongement.setDateProlongement(LocalDate.now());

            // Appel du service pour enregistrer le prolongement
            prolongementService.save(prolongement);
            redirectAttributes.addFlashAttribute("successMessage", "Prolongement ajouté avec succès.");
        } catch (DateTimeParseException e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Format de date invalide. Utilisez le format yyyy-MM-dd'T'HH:mm (ex. 2025-07-03T20:30)." );
            return "redirect:/prolongement/form?id=" + idEmprunt;
        } catch (EmpruntException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/prolongement/form?id=" + idEmprunt;
        }

        return "redirect:/prolongement/liste";
    }
}