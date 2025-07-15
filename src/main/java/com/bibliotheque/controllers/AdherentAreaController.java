package com.bibliotheque.controllers;

import com.bibliotheque.entities.*;
import com.bibliotheque.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/adherent")
public class AdherentAreaController {
    @Autowired
    private AdherentService adherentService;
    @Autowired
    private EmpruntService empruntService;
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private PenaliteService penaliteService;
    @Autowired
    private AbonnementService abonnementService;
    @Autowired
    private ProfilsAdherentService profilsAdherentService;

    @GetMapping("/dashboard")
    public ModelAndView dashboard(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return new ModelAndView("redirect:/login");
        }
        Optional<Adherent> adherentOpt = adherentService.findByUtilisateurId(userId);
        if (adherentOpt.isEmpty()) {
            return new ModelAndView("redirect:/login");
        }
        Adherent adherent = adherentOpt.get();
        int adherentId = adherent.getId();
        // Emprunts en cours
        List<Emprunt> emprunts = empruntService.getEmpruntsEnCours(adherentId);
        // Réservations
        List<Reservation> reservations = reservationService.getAll();
        long nbReservations = reservations.stream().filter(r -> r.getAdherent().getId().equals(adherentId)).count();
        // Pénalités actives
        List<Penalite> penalites = penaliteService.repository.findActivePenalitesByAdherent(adherentId, java.time.LocalDate.now());
        // Abonnement actif
        boolean abonnementActif = abonnementService.repository.findActiveAbonnementByAdherent(adherentId, java.time.LocalDate.now()).isPresent();

        ModelAndView mv = new ModelAndView("adherent/template");
        mv.addObject("nbEmpruntsEnCours", emprunts.size());
        mv.addObject("nbReservations", nbReservations);
        mv.addObject("nbPenalitesActives", penalites.size());
        mv.addObject("abonnementActif", abonnementActif);
        mv.addObject("contentPage", "dashboard.jsp");
        return mv;
    }

    @GetMapping("/emprunts")
    public ModelAndView emprunts(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return new ModelAndView("redirect:/login");
        }
        Optional<Adherent> adherentOpt = adherentService.findByUtilisateurId(userId);
        if (adherentOpt.isEmpty()) {
            return new ModelAndView("redirect:/login");
        }
        Adherent adherent = adherentOpt.get();
        int adherentId = adherent.getId();
        List<Emprunt> emprunts = empruntService.getEmpruntsEnCours(adherentId);
        // Statuts
        Map<Integer, String> statuts = new HashMap<>();
        for (Emprunt emprunt : emprunts) {
            String statut = empruntService.getLastStatutForEmprunt(emprunt.getId());
            statuts.put(emprunt.getId(), statut);
        }
        ModelAndView mv = new ModelAndView("adherent/template");
        mv.addObject("emprunts", emprunts);
        mv.addObject("statuts", statuts);
        mv.addObject("contentPage", "emprunts.jsp");
        return mv;
    }

    @GetMapping("/reservations")
    public ModelAndView reservations(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return new ModelAndView("redirect:/login");
        }
        Optional<Adherent> adherentOpt = adherentService.findByUtilisateurId(userId);
        if (adherentOpt.isEmpty()) {
            return new ModelAndView("redirect:/login");
        }
        Adherent adherent = adherentOpt.get();
        int adherentId = adherent.getId();
        List<Reservation> reservations = reservationService.getAll();
        List<Reservation> adherentReservations = new ArrayList<>();
        Map<Integer, String> statuts = new HashMap<>();
        for (Reservation reservation : reservations) {
            if (reservation.getAdherent().getId().equals(adherentId)) {
                adherentReservations.add(reservation);
                String statut = reservationService.getLastStatutForReservation(reservation.getId());
                statuts.put(reservation.getId(), statut);
            }
        }
        ModelAndView mv = new ModelAndView("adherent/template");
        mv.addObject("reservations", adherentReservations);
        mv.addObject("statuts", statuts);
        mv.addObject("contentPage", "reservations.jsp");
        return mv;
    }

    @GetMapping("/profil")
    public ModelAndView profil(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return new ModelAndView("redirect:/login");
        }
        Optional<Adherent> adherentOpt = adherentService.findByUtilisateurId(userId);
        if (adherentOpt.isEmpty()) {
            return new ModelAndView("redirect:/login");
        }
        Adherent adherent = adherentOpt.get();
        ProfilsAdherent profil = profilsAdherentService.repository.findById(adherent.getIdProfil()).orElse(null);
        Integer quota = profil != null ? profil.getQuotaEmpruntsSimultanes() : null;
        List<Penalite> penalites = penaliteService.repository.findByAdherentId(adherent.getId());
        boolean abonnementActif = abonnementService.repository.findActiveAbonnementByAdherent(adherent.getId(), java.time.LocalDate.now()).isPresent();
        ModelAndView mv = new ModelAndView("adherent/template");
        mv.addObject("adherent", adherent);
        mv.addObject("profil", profil);
        mv.addObject("quota", quota);
        mv.addObject("penalites", penalites);
        mv.addObject("abonnementActif", abonnementActif);
        mv.addObject("contentPage", "profil.jsp");
        return mv;
    }
} 