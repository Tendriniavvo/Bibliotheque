package com.bibliotheque.controllers;

import com.bibliotheque.entities.*;
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
public class EmpruntController {
    
}
