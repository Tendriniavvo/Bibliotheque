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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        // Créer une map pour associer chaque emprunt à son dernier statut
        Map<Integer, String> statuts = new HashMap<>();
        for (Emprunt emprunt : emprunts) {
            String statut = empruntService.getLastStatutForEmprunt(emprunt.getId());
            statuts.put(emprunt.getId(), statut);
        }
        ModelAndView mv = new ModelAndView("bibliothecaire/template");
        mv.addObject("emprunts", emprunts);
        mv.addObject("statuts", statuts); // Ajouter les statuts au modèle
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
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Les identifiants de l'adhérent, de l'exemplaire ou du type d'emprunt sont manquants.");
            return "redirect:/emprunt/form";
        }

        if (dateEmpruntStr == null || dateEmpruntStr.isEmpty() || dateRetourPrevueStr == null
                || dateRetourPrevueStr.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Les dates d'emprunt ou de retour prévu sont manquantes.");
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
            LocalDate dateEmprunt = LocalDate.parse(dateEmpruntStr, formatter);
            LocalDate dateRetourPrevue = LocalDate.parse(dateRetourPrevueStr, formatter);
            emprunt.setDateEmprunt(dateEmprunt);
            emprunt.setDateRetourPrevue(dateRetourPrevue);

            // Appel du service pour enregistrer l'emprunt
            empruntService.save(emprunt);
            redirectAttributes.addFlashAttribute("successMessage", "Emprunt ajouté avec succès.");
        } catch (DateTimeParseException e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Format de date invalide. Utilisez le format yyyy-MM-dd'T'HH:mm (ex. 2025-07-03T20:30).");
            return "redirect:/emprunt/form";
        } catch (EmpruntException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/emprunt/form";
        }

        return "redirect:/emprunt/liste";
    }

    @GetMapping("/prolonger/id")
    public String prolongerEmprunt(
            @RequestParam("id") Integer idEmprunt,
            RedirectAttributes redirectAttributes) {

        Optional<Emprunt> empruntOpt = empruntService.findById(idEmprunt);
        if (empruntOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "L'emprunt spécifié n'existe pas.");
            return "redirect:/emprunt/liste";
        }

        Emprunt emprunt = empruntOpt.get();
        if (!"En cours".equalsIgnoreCase(empruntService.getLastStatutForEmprunt(emprunt.getId()))) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "L'emprunt n'est pas en cours et ne peut pas être prolongé.");
            return "redirect:/emprunt/liste";
        }

        // Logique de prolongement à implémenter ici

        return "redirect:/emprunt/liste";
    }

    @GetMapping("/rendre")
    public String rendreEmprunt(
            @RequestParam("idEmprunt") Integer idEmprunt,
            RedirectAttributes redirectAttributes) {
    
        // if (idEmprunt == null) {
        //     redirectAttributes.addFlashAttribute("errorMessage", "ID d'emprunt manquant.");
        //     return "redirect:/emprunt/liste";
        // }
    
        try {
            LocalDate dateRetourEffective = LocalDate.now();
            empruntService.rendreEmprunt(idEmprunt, dateRetourEffective);
    
            redirectAttributes.addFlashAttribute("successMessage", "L'emprunt a été rendu avec succès.");
            return "redirect:/emprunt/liste";
        } catch (EmpruntException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Une erreur est survenue lors du retour de l'emprunt.");
        }
    
        return "redirect:/emprunt/liste";
    }
    

    
}