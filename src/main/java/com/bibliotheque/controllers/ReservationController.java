package com.bibliotheque.controllers;

import com.bibliotheque.entities.*;
import com.bibliotheque.services.*;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private LivreService livreService;

    @Autowired
    private AdherentService adherentService;

    @Autowired
    private StatutReservationService statutReservationService;

    // Afficher la liste des réservations
    @GetMapping("/liste")
    public ModelAndView listeReservations() {
        List<Reservation> reservations = reservationService.getAll();
        ModelAndView mv = new ModelAndView("bibliothecaire/template");
        mv.addObject("reservations", reservations);
        mv.addObject("contentPage", "reservationListe.jsp");
        return mv;
    }

    // Afficher le formulaire d'ajout ou modification
    @GetMapping({ "/form", "/form/{id}" })
    public ModelAndView formReservation(@PathVariable(required = false) Integer id) {
        ModelAndView mv = new ModelAndView("bibliothecaire/template");

        List<Livre> livres = livreService.getAll();
        List<Adherent> adherents = adherentService.getAll();
        List<StatutReservation> statuts = statutReservationService.getAll();

        mv.addObject("livres", livres);
        mv.addObject("adherents", adherents);
        mv.addObject("statuts", statuts);

        if (id != null) {
            Optional<Reservation> reservationOpt = reservationService.findById(id);
            if (reservationOpt.isPresent()) {
                mv.addObject("reservation", reservationOpt.get());
            } else {
                // redirection si id non trouvé
                return new ModelAndView("redirect:/reservation/liste");
            }
        }

        mv.addObject("contentPage", "reservationForm.jsp");
        return mv;
    }

    // Sauvegarder une nouvelle réservation
    @PostMapping("/save")
public String saveReservation(
        @RequestParam("idLivre") Integer idLivre,
        @RequestParam("idAdherent") Integer idAdherent,
        @RequestParam("idStatut") Integer idStatut,
        @RequestParam("dateExpiration") String dateExpirationStr,
        HttpServletRequest request,
        RedirectAttributes redirectAttributes) {

    try {
        Optional<Livre> livreOpt = livreService.findById(idLivre);
        Optional<Adherent> adherentOpt = adherentService.findById(idAdherent);
        Optional<StatutReservation> statutOpt = statutReservationService.findById(idStatut);

        if (livreOpt.isEmpty() || adherentOpt.isEmpty() || statutOpt.isEmpty()) {
            request.setAttribute("message", "Entité introuvable.");
            request.setAttribute("messageType", "error");
            request.setAttribute("livres", livreService.getAll());
            request.setAttribute("adherents", adherentService.getAll());
            request.setAttribute("statuts", statutReservationService.getAll());
            request.setAttribute("contentPage", "reservationForm.jsp");
            return "bibliothecaire/template";
        }

        Reservation reservation = new Reservation();
        reservation.setLivre(livreOpt.get());
        reservation.setAdherent(adherentOpt.get());
        reservation.setStatut(statutOpt.get());
        reservation.setDateDemande(java.time.Instant.now());
        reservation.setDateExpiration(LocalDate.parse(dateExpirationStr));

        reservationService.save(reservation);

        redirectAttributes.addFlashAttribute("message", "Réservation enregistrée avec succès.");
        redirectAttributes.addFlashAttribute("messageType", "success");

        return "redirect:/reservation/liste";

    } catch (Exception e) {
        request.setAttribute("message", "Erreur lors de l'enregistrement : " + e.getMessage());
        request.setAttribute("messageType", "error");
        request.setAttribute("livres", livreService.getAll());
        request.setAttribute("adherents", adherentService.getAll());
        request.setAttribute("statuts", statutReservationService.getAll());
        request.setAttribute("contentPage", "reservationForm.jsp");
        return "bibliothecaire/template"; // pas de redirect ici
    }
}


    // Mettre à jour une réservation existante
    @PostMapping("/update")
    public String updateReservation(
            @RequestParam("id") Integer id,
            @RequestParam("idLivre") Integer idLivre,
            @RequestParam("idAdherent") Integer idAdherent,
            @RequestParam("idStatut") Integer idStatut,
            @RequestParam("dateExpiration") String dateExpirationStr) {

        Optional<Reservation> reservationOpt = reservationService.findById(id);
        Optional<Livre> livreOpt = livreService.findById(idLivre);
        Optional<Adherent> adherentOpt = adherentService.findById(idAdherent);
        Optional<StatutReservation> statutOpt = statutReservationService.findById(idStatut);

        if (reservationOpt.isEmpty() || livreOpt.isEmpty() || adherentOpt.isEmpty() || statutOpt.isEmpty()) {
            return "redirect:/reservation/form/" + id + "?error=entityNotFound";
        }

        Reservation reservation = reservationOpt.get();
        reservation.setLivre(livreOpt.get());
        reservation.setAdherent(adherentOpt.get());
        reservation.setStatut(statutOpt.get());
        reservation.setDateExpiration(LocalDate.parse(dateExpirationStr));

        reservationService.save(reservation);

        return "redirect:/reservation/liste";
    }

    // Supprimer une réservation
    @GetMapping("/delete")
    public String deleteReservation(@RequestParam("id") Integer id) {
        reservationService.deleteById(id);
        return "redirect:/reservation/liste";
    }
}
