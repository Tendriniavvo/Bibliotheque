package com.bibliotheque.controllers;

import com.bibliotheque.entities.Adherent;
import com.bibliotheque.entities.Emprunt;
import com.bibliotheque.entities.Exemplaire;
import com.bibliotheque.entities.TypeEmprunt;
import com.bibliotheque.services.AdherentService;
import com.bibliotheque.services.EmpruntService;
import com.bibliotheque.services.ExemplaireService;
import com.bibliotheque.services.TypeEmpruntService;
import com.bibliotheque.exceptions.EmpruntException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/emprunt")
public class EmpruntController {

    private final EmpruntService empruntService;
    private final AdherentService adherentService;
    private final ExemplaireService exemplaireService;
    private final TypeEmpruntService typeEmpruntService;

    @Autowired
    public EmpruntController(
            EmpruntService empruntService,
            AdherentService adherentService,
            ExemplaireService exemplaireService,
            TypeEmpruntService typeEmpruntService) {
        this.empruntService = empruntService;
        this.adherentService = adherentService;
        this.exemplaireService = exemplaireService;
        this.typeEmpruntService = typeEmpruntService;
    }

    @GetMapping("/liste")
    public ModelAndView listeEmprunt() {
        List<Emprunt> emprunts = empruntService.findAll();
        ModelAndView mv = new ModelAndView("bibliothecaire/template");
        mv.addObject("emprunts", emprunts);
        mv.addObject("contentPage", "empruntListe.jsp");
        return mv;
    }

    @GetMapping("/form")
    public ModelAndView formAuteur() {
        ModelAndView mv = new ModelAndView("bibliothecaire/template");
        List<Adherent> adherents = adherentService.getAll();
        List<Exemplaire> exemplaires = exemplaireService.getAll();
        List<TypeEmprunt> typeEmprunts = typeEmpruntService.getAll();
        mv.addObject("adherents", adherents);
        mv.addObject("exemplaires", exemplaires);
        mv.addObject("typesEmprunt", typeEmprunts);
        mv.addObject("contentPage", "empruntForm.jsp");
        return mv;
    }

    @PostMapping("/save")
    public String saveEmprunt(
            @RequestParam("idAdherent") Integer idAdherent,
            @RequestParam("idExemplaire") Integer idExemplaire,
            @RequestParam("idTypeEmprunt") Integer idTypeEmprunt,
            @RequestParam("dateEmprunt") String dateEmpruntStr,
            @RequestParam("dateRetourPrevue") String dateRetourPrevueStr,
            RedirectAttributes redirectAttributes) {

        // Validation des paramètres de base
        if (idAdherent == null || idExemplaire == null || idTypeEmprunt == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Les identifiants de l'adhérent, de l'exemplaire ou du type d'emprunt sont manquants.");
            return "redirect:/emprunt/form";
        }

        if (dateEmpruntStr == null || dateEmpruntStr.isEmpty() || dateRetourPrevueStr == null || dateRetourPrevueStr.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Les dates d'emprunt ou de retour prévu sont manquantes.");
            return "redirect:/emprunt/form";
        }

        // Création de l'objet Emprunt
        Emprunt emprunt = new Emprunt();
        try {
            // Récupération des entités
            Adherent adherent = adherentService.findById(idAdherent)
                    .orElseThrow(() -> new EmpruntException("L'adhérent spécifié n'existe pas."));
            Exemplaire exemplaire = exemplaireService.findById(idExemplaire)
                    .orElseThrow(() -> new EmpruntException("L'exemplaire spécifié n'existe pas."));
            TypeEmprunt typeEmprunt = typeEmpruntService.findById(idTypeEmprunt)
                    .orElseThrow(() -> new EmpruntException("Le type d'emprunt spécifié n'existe pas."));

            emprunt.setAdherent(adherent);
            emprunt.setExemplaire(exemplaire);
            emprunt.setTypeEmprunt(typeEmprunt);

            // Conversion des dates
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            Instant dateEmprunt = LocalDateTime.parse(dateEmpruntStr, formatter).atZone(ZoneId.systemDefault()).toInstant();
            Instant dateRetourPrevue = LocalDateTime.parse(dateRetourPrevueStr, formatter).atZone(ZoneId.systemDefault()).toInstant();

            emprunt.setDateEmprunt(dateEmprunt);
            emprunt.setDateRetourPrevue(dateRetourPrevue);

            // Appel du service pour enregistrer l'emprunt
            empruntService.save(emprunt);
            redirectAttributes.addFlashAttribute("successMessage", "Emprunt ajouté avec succès.");
        } catch (DateTimeParseException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Format de date invalide. Utilisez le format yyyy-MM-dd'T'HH:mm (ex. 2025-07-03T20:30).");
            return "redirect:/emprunt/form";
        } catch (EmpruntException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/emprunt/form";
        }

        return "redirect:/emprunt/liste";
    }
}