package com.bibliotheque.services;

import com.bibliotheque.entities.Adherent;
import com.bibliotheque.entities.Utilisateur;
import com.bibliotheque.repositories.AdherentRepository;
import com.bibliotheque.repositories.UtilisateurRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final AdherentRepository adherentRepository;

    public UtilisateurService(UtilisateurRepository utilisateurRepository, AdherentRepository adherentRepository) {
        this.utilisateurRepository = utilisateurRepository;
        this.adherentRepository = adherentRepository;
    }

    public Optional<Utilisateur> findByEmail(String email) {
        return utilisateurRepository.findByEmail(email);
    }

    public boolean verifierConnexion(String email, String motDePasse) {
        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findByEmail(email);
        return utilisateurOpt.isPresent() && utilisateurOpt.get().getMotDePasseHash().equals(motDePasse);
    }

    public Utilisateur save(Utilisateur utilisateur) {
        return utilisateurRepository.save(utilisateur);
    }

    public Adherent saveAdherent(Adherent adherent) {
        return adherentRepository.save(adherent);
    }
}
