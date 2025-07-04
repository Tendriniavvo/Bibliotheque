package com.bibliotheque.controllers;

import com.bibliotheque.entities.*;
import com.bibliotheque.exceptions.ReservationException;
import com.bibliotheque.services.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Autowired
    private BibliothecaireService bibliothecaireService;

    // Afficher la liste des réservations
    @GetMapping("/liste")
    public ModelAndView listeReservations(HttpSession session) {
        List<Reservation> reservations = reservationService.getAll();
        ModelAndView mv = new ModelAndView();
        Integer userId = (Integer) session.getAttribute("userId");
        String template = bibliothecaireService.findByUtilisateurId(userId).isPresent()
                ? "bibliothecaire/template"
                : "adherent/template";
        mv.setViewName(template);
        mv.addObject("reservations", reservations);
        mv.addObject("contentPage", "reservationListe.jsp");
        return mv;
    }

    // Afficher le formulaire d'ajout ou modification
    @GetMapping({ "/form", "/form/{id}" })
    public ModelAndView formReservation(@PathVariable(required = false) Integer id, HttpSession session) {
        ModelAndView mv = new ModelAndView();

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
                mv.addObject("message", "Réservation introuvable.");
                mv.addObject("messageType", "error");
                mv.addObject("contentPage", "reservationListe.jsp");
                Integer userId = (Integer) session.getAttribute("userId");
                String template = bibliothecaireService.findByUtilisateurId(userId).isPresent()
                        ? "bibliothecaire/template"
                        : "adherent/template";
                mv.setViewName(template);
                return mv;
            }
        }

        // Déterminer le template en fonction du type d'utilisateur
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId != null && bibliothecaireService.findByUtilisateurId(userId).isPresent()) {
            mv.setViewName("bibliothecaire/template");
        } else {
            Optional<Adherent> adherentOpt = adherentService.findByUtilisateurId(userId);
            mv.addObject("profil", adherentOpt.orElse(null));
            mv.setViewName("adherent/template");
        }

        mv.addObject("contentPage", "reservationForm.jsp");
        return mv;
    }

    // Sauvegarder une nouvelle réservation
    @PostMapping("/save")
    public String saveReservation(
            @RequestParam("idLivre") Integer idLivre,
            @RequestParam("idAdherent") Integer idAdherent,
            @RequestParam("dateAReserver") String dateAReserver,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes,
            HttpSession session) {

        try {
            // Vérifier que l'adhérent connecté correspond à idAdherent (pour les adhérents)
            Integer userId = (Integer) session.getAttribute("userId");
            Optional<Adherent> adherentFromSession = adherentService.findByUtilisateurId(userId);
            if (!bibliothecaireService.findByUtilisateurId(userId).isPresent() &&
                (adherentFromSession.isEmpty() || !adherentFromSession.get().getId().equals(idAdherent))) {
                request.setAttribute("message", "Vous ne pouvez pas réserver pour un autre adhérent.");
                request.setAttribute("messageType", "error");
                request.setAttribute("livres", livreService.getAll());
                request.setAttribute("adherents", adherentService.getAll());
                request.setAttribute("statuts", statutReservationService.getAll());
                request.setAttribute("profil", adherentFromSession.orElse(null));
                request.setAttribute("contentPage", "reservationForm.jsp");
                return "adherent/template";
            }

            Optional<Livre> livreOpt = livreService.findById(idLivre);
            Optional<Adherent> adherentOpt = adherentService.findById(idAdherent);

            if (livreOpt.isEmpty() || adherentOpt.isEmpty()) {
                request.setAttribute("message", "Livre ou adhérent introuvable.");
                request.setAttribute("messageType", "error");
                request.setAttribute("livres", livreService.getAll());
                request.setAttribute("adherents", adherentService.getAll());
                request.setAttribute("statuts", statutReservationService.getAll());
                request.setAttribute("profil", adherentFromSession.orElse(null));
                String template = bibliothecaireService.findByUtilisateurId(userId).isPresent()
                        ? "bibliothecaire/template"
                        : "adherent/template";
                request.setAttribute("contentPage", "reservationForm.jsp");
                return template;
            }

            Reservation reservation = new Reservation();
            reservation.setLivre(livreOpt.get());
            reservation.setAdherent(adherentOpt.get());
            reservation.setDateDemande(LocalDateTime.now());

            // Conversion de la chaîne dateAReserver en LocalDateTime
            LocalDate localDate = LocalDate.parse(dateAReserver);
            LocalDateTime dateAReserverLocalDateTime = localDate.atStartOfDay();
            reservation.setDateAReserver(dateAReserverLocalDateTime);

            reservationService.save(reservation);

            redirectAttributes.addFlashAttribute("message", "Réservation enregistrée avec succès.");
            redirectAttributes.addFlashAttribute("messageType", "success");

            return "redirect:/reservation/liste";

        } catch (ReservationException e) {
            request.setAttribute("message", e.getMessage());
            request.setAttribute("messageType", "error");
            request.setAttribute("livres", livreService.getAll());
            request.setAttribute("adherents", adherentService.getAll());
            request.setAttribute("statuts", statutReservationService.getAll());

            Integer userId = (Integer) session.getAttribute("userId");
            request.setAttribute("profil", adherentService.findByUtilisateurId(userId).orElse(null));
            String template = bibliothecaireService.findByUtilisateurId(userId).isPresent()
                    ? "bibliothecaire/template"
                    : "adherent/template";
            request.setAttribute("contentPage", "reservationForm.jsp");
            return template;
        } catch (Exception e) {
            request.setAttribute("message", "Erreur inattendue lors de l'enregistrement : " + e.getMessage());
            request.setAttribute("messageType", "error");
            request.setAttribute("livres", livreService.getAll());
            request.setAttribute("adherents", adherentService.getAll());
            request.setAttribute("statuts", statutReservationService.getAll());

            Integer userId = (Integer) session.getAttribute("userId");
            request.setAttribute("profil", adherentService.findByUtilisateurId(userId).orElse(null));
            String template = bibliothecaireService.findByUtilisateurId(userId).isPresent()
                    ? "bibliothecaire/template"
                    : "adherent/template";
            request.setAttribute("contentPage", "reservationForm.jsp");
            return template;
        }
    }

    // Mettre à jour une réservation existante
    @PostMapping("/update")
    public String updateReservation(
            @RequestParam("id") Integer id,
            @RequestParam("idLivre") Integer idLivre,
            @RequestParam("idAdherent") Integer idAdherent,
            @RequestParam("dateAReserver") String dateAReserver,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes,
            HttpSession session) {

        try {
            // Vérifier que l'adhérent connecté correspond à idAdherent (pour les adhérents)
            Integer userId = (Integer) session.getAttribute("userId");
            Optional<Adherent> adherentFromSession = adherentService.findByUtilisateurId(userId);
            if (!bibliothecaireService.findByUtilisateurId(userId).isPresent() &&
                (adherentFromSession.isEmpty() || !adherentFromSession.get().getId().equals(idAdherent))) {
                request.setAttribute("message", "Vous ne pouvez pas modifier la réservation d'un autre adhérent.");
                request.setAttribute("messageType", "error");
                request.setAttribute("livres", livreService.getAll());
                request.setAttribute("adherents", adherentService.getAll());
                request.setAttribute("statuts", statutReservationService.getAll());
                request.setAttribute("reservation", reservationService.findById(id).orElse(null));
                request.setAttribute("profil", adherentFromSession.orElse(null));
                request.setAttribute("contentPage", "reservationForm.jsp");
                return "adherent/template";
            }

            Optional<Reservation> reservationOpt = reservationService.findById(id);
            Optional<Livre> livreOpt = livreService.findById(idLivre);
            Optional<Adherent> adherentOpt = adherentService.findById(idAdherent);

            if (reservationOpt.isEmpty() || livreOpt.isEmpty() || adherentOpt.isEmpty()) {
                request.setAttribute("message", "Réservation, livre ou adhérent introuvable.");
                request.setAttribute("messageType", "error");
                request.setAttribute("livres", livreService.getAll());
                request.setAttribute("adherents", adherentService.getAll());
                request.setAttribute("statuts", statutReservationService.getAll());
                request.setAttribute("reservation", reservationOpt.orElse(null));
                request.setAttribute("profil", adherentFromSession.orElse(null));
                String template = bibliothecaireService.findByUtilisateurId(userId).isPresent()
                        ? "bibliothecaire/template"
                        : "adherent/template";
                request.setAttribute("contentPage", "reservationForm.jsp");
                return template;
            }

            Reservation reservation = reservationOpt.get();
            reservation.setLivre(livreOpt.get());
            reservation.setAdherent(adherentOpt.get());
            LocalDate localDate = LocalDate.parse(dateAReserver);
            LocalDateTime dateAReserverLocalDateTime = localDate.atStartOfDay();
            reservation.setDateAReserver(dateAReserverLocalDateTime);

            reservationService.save(reservation);

            redirectAttributes.addFlashAttribute("message", "Réservation mise à jour avec succès.");
            redirectAttributes.addFlashAttribute("messageType", "success");

            return "redirect:/reservation/liste";

        } catch (ReservationException e) {
            request.setAttribute("message", e.getMessage());
            request.setAttribute("messageType", "error");
            request.setAttribute("livres", livreService.getAll());
            request.setAttribute("adherents", adherentService.getAll());
            request.setAttribute("statuts", statutReservationService.getAll());
            request.setAttribute("reservation", reservationService.findById(id).orElse(null));
            Integer userId = (Integer) session.getAttribute("userId");
            request.setAttribute("profil", adherentService.findByUtilisateurId(userId).orElse(null));
            String template = bibliothecaireService.findByUtilisateurId(userId).isPresent()
                    ? "bibliothecaire/template"
                    : "adherent/template";
            request.setAttribute("contentPage", "reservationForm.jsp");
            return template;
        } catch (Exception e) {
            request.setAttribute("message", "Erreur inattendue lors de la mise à jour : " + e.getMessage());
            request.setAttribute("messageType", "error");
            request.setAttribute("livres", livreService.getAll());
            request.setAttribute("adherents", adherentService.getAll());
            request.setAttribute("statuts", statutReservationService.getAll());
            request.setAttribute("reservation", reservationService.findById(id).orElse(null));
            Integer userId = (Integer) session.getAttribute("userId");
            request.setAttribute("profil", adherentService.findByUtilisateurId(userId).orElse(null));
            String template = bibliothecaireService.findByUtilisateurId(userId).isPresent()
                    ? "bibliothecaire/template"
                    : "adherent/template";
            request.setAttribute("contentPage", "reservationForm.jsp");
            return template;
        }
    }

    // Supprimer une réservation
    @GetMapping("/delete")
    public String deleteReservation(@RequestParam("id") Integer id, RedirectAttributes redirectAttributes, HttpSession session) {
        try {
            // Vérifier que l'adhérent connecté peut supprimer la réservation
            Integer userId = (Integer) session.getAttribute("userId");
            Optional<Adherent> adherentFromSession = adherentService.findByUtilisateurId(userId);
            if (!bibliothecaireService.findByUtilisateurId(userId).isPresent()) {
                Optional<Reservation> reservationOpt = reservationService.findById(id);
                if (reservationOpt.isEmpty() || 
                    (adherentFromSession.isEmpty() || !adherentFromSession.get().getId().equals(reservationOpt.get().getAdherent().getId()))) {
                    redirectAttributes.addFlashAttribute("message", "Vous ne pouvez pas supprimer la réservation d'un autre adhérent.");
                    redirectAttributes.addFlashAttribute("messageType", "error");
                    return "redirect:/reservation/liste";
                }
            }

            reservationService.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Réservation supprimée avec succès.");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Erreur lors de la suppression : " + e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
        }
        return "redirect:/reservation/liste";
    }
}