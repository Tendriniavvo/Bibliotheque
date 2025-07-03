package com.bibliotheque.controllers;

import com.bibliotheque.entities.Adherent;
import com.bibliotheque.entities.Bibliothecaire;
import com.bibliotheque.entities.Utilisateur;
import com.bibliotheque.models.RegisterForm;
import com.bibliotheque.services.AdherentService;
import com.bibliotheque.services.BibliothecaireService;
import com.bibliotheque.services.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private AdherentService adherentService;

    @Autowired
    private BibliothecaireService bibliothecaireService;

    @PostMapping("/login")
    public ModelAndView login(
            @RequestParam String email,
            @RequestParam String motDePasseHash,
            HttpSession session) {

        Optional<Utilisateur> utilisateurOpt = utilisateurService.findByEmail(email);

        if (utilisateurOpt.isEmpty() || 
            !utilisateurOpt.get().getMotDePasseHash().equals(motDePasseHash)) {
            ModelAndView mv = new ModelAndView("login");
            mv.addObject("error", "Identifiants invalides");
            return mv;
        }

        Utilisateur utilisateur = utilisateurOpt.get();

        session.setAttribute("userId", utilisateur.getId());

        Optional<Bibliothecaire> bibliothecaireOpt = bibliothecaireService.findByUtilisateurId(utilisateur.getId());
        if (bibliothecaireOpt.isPresent()) {
            ModelAndView mv = new ModelAndView("bibliothecaire/template");
            mv.addObject("contentPage", "dashboard.jsp");
            mv.addObject("bibliothecaire", bibliothecaireOpt.get());
            return mv;
        }
        ModelAndView mv = new ModelAndView("adherent/template");
        mv.addObject("contentPage", "dash.jsp");
        Optional<Adherent> adherentOpt = adherentService.findByUtilisateurId(utilisateur.getId());
        mv.addObject("profil", adherentOpt.get());
        return mv;
    }

    @PostMapping("/register")
    public ModelAndView register(@ModelAttribute RegisterForm form) {
        ModelAndView mv = new ModelAndView();

        Optional<Utilisateur> existingUser = utilisateurService.findByEmail(form.getEmail());
        if (existingUser.isPresent()) {
            mv.setViewName("auth/signup");
            mv.addObject("error", "Cet email est déjà utilisé.");
            return mv;
        }

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail(form.getEmail());
        utilisateur.setMotDePasseHash(form.getMotDePasseHash());
        utilisateur.setDateCreation(Instant.now());
        Utilisateur savedUser = utilisateurService.save(utilisateur);

        if ("Client".equalsIgnoreCase(form.getProfilType())) {
            Adherent adherent = new Adherent();
            adherent.setIdUtilisateur(savedUser.getId());
            adherent.setNom(form.getNom());
            adherent.setPrenom(form.getPrenom());
            adherent.setDateNaissance(LocalDate.parse(form.getDateNaissance()));
            adherent.setDateInscription(LocalDate.now());
            adherent.setIdProfil(form.getIdProfil());
            adherentService.save(adherent);
        } else if ("Bibliothecaire".equalsIgnoreCase(form.getProfilType())) {
            Bibliothecaire biblio = new Bibliothecaire();
            biblio.setIdUtilisateur(savedUser.getId());
            biblio.setNom(form.getNom());
            biblio.setPrenom(form.getPrenom());
            bibliothecaireService.save(biblio);
        }

        mv.setViewName("auth/signup");
        mv.addObject("email", form.getEmail());
        return mv;
    }
}
