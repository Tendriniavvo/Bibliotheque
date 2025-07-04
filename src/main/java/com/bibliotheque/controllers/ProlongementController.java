package com.bibliotheque.controllers;

import com.bibliotheque.entities.Emprunt;
import com.bibliotheque.entities.Prolongement;
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

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
        List<Emprunt> emprunts = empruntService.findAll();
        ModelAndView mv = new ModelAndView("bibliothecaire/template");
        mv.addObject("emprunts", emprunts);
        mv.addObject("contentPage", "prolongementForm.jsp");
        return mv;
    }

    @PostMapping("/save")
    public String saveProlongement(
            @RequestParam("idEmprunt") Integer idEmprunt,
            @RequestParam("dateFin") String dateFinStr,
            RedirectAttributes redirectAttributes) {

        // Validation des paramètres
        if (idEmprunt == null || dateFinStr == null || dateFinStr.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "L'identifiant de l'emprunt ou la date de fin est manquant.");
            return "redirect:/prolongement/form";
        }

        // Vérification de l'existence de l'emprunt
        Optional<Emprunt> empruntOpt = empruntService.findById(idEmprunt);
        if (empruntOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "L'emprunt spécifié n'existe pas.");
            return "redirect:/prolongement/form";
        }

        Emprunt emprunt = empruntOpt.get();
        // Vérification du statut de l'emprunt
        if (!"En cours".equalsIgnoreCase(empruntService.getLastStatutForEmprunt(emprunt.getId()))) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "L'emprunt n'est pas en cours et ne peut pas être prolongé.");
            return "redirect:/prolongement/form";
        }

        // Conversion de la date de fin
        LocalDate dateFin;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime ldt = LocalDateTime.parse(dateFinStr, formatter);
            dateFin = ldt.toLocalDate();
        } catch (DateTimeParseException e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Format de date invalide. Utilisez le format yyyy-MM-dd'T'HH:mm (ex. 2025-07-03T20:30).");
            return "redirect:/prolongement/form";
        }

        // Vérification que la nouvelle date de fin est postérieure à la date de retour
        // prévue actuelle
        LocalDate ancienneDateFin = emprunt.getDateRetourPrevue();
        if (dateFin.isBefore(ancienneDateFin) || dateFin.equals(ancienneDateFin)) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "La nouvelle date de fin doit être postérieure à la date de retour prévue actuelle.");
            return "redirect:/prolongement/form";
        }

        try {
            // Création du prolongement
            Prolongement prolongement = new Prolongement();
            prolongement.setEmprunt(emprunt);
            prolongement.setDateFin(dateFin);
            prolongement.setDateProlongement(LocalDate.now());

            // Enregistrement du prolongement
            prolongementService.save(prolongement);

            // Mise à jour de la date de retour prévue de l'emprunt
            emprunt.setDateRetourPrevue(dateFin);
            empruntService.save(emprunt);

            redirectAttributes.addFlashAttribute("successMessage", "Prolongement ajouté avec succès.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Erreur lors de l'enregistrement du prolongement : " + e.getMessage());
            return "redirect:/prolongement/form";
        }

        return "redirect:/prolongement/liste";
    }
}