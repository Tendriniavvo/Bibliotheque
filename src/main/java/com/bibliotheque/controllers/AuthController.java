package com.bibliotheque.controllers;

import com.bibliotheque.entities.Adherent;
import com.bibliotheque.entities.Utilisateur;
import com.bibliotheque.models.RegisterForm;
import com.bibliotheque.services.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private UtilisateurService utilisateurService;

    @PostMapping("/login")
    public ModelAndView login(
            @RequestParam String email,
            @RequestParam String motDePasseHash) {
        if (!utilisateurService.verifierConnexion(email, motDePasseHash)) {
            ModelAndView mv = new ModelAndView("login");
            mv.addObject("error", "Identifiants invalides");
            return mv;
        }

        Utilisateur utilisateur = utilisateurService.findByEmail(email).get();

        ModelAndView mv = new ModelAndView("test");
        mv.addObject("email", utilisateur.getEmail());
        return mv;
    }

    @PostMapping("/register")
    public ModelAndView register(@ModelAttribute RegisterForm form) {
        Optional<Utilisateur> existingUser = utilisateurService.findByEmail(form.getEmail());
        if (existingUser.isPresent()) {
            ModelAndView mv = new ModelAndView("register");
            mv.addObject("error", "Cet email est déjà utilisé.");
            return mv;
        }

        // Étape 1 : Créer l'utilisateur
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail(form.getEmail());
        utilisateur.setMotDePasseHash(form.getMotDePasseHash());
        utilisateur.setDateCreation(Instant.now()); // ✅ bon type
        Utilisateur savedUser = utilisateurService.save(utilisateur);

        // Étape 2 : Créer l'adhérent
        Adherent adherent = new Adherent();
        adherent.setIdUtilisateur(savedUser.getId());
        adherent.setNom(form.getNom());
        adherent.setPrenom(form.getPrenom());
        adherent.setDateNaissance(LocalDate.parse(form.getDateNaissance()));
        adherent.setDateInscription(java.time.LocalDate.now());
        adherent.setIdProfil(form.getIdProfil());
        utilisateurService.saveAdherent(adherent);

        ModelAndView mv = new ModelAndView("test");
        mv.addObject("email", form.getEmail());
        return mv;
    }

}
