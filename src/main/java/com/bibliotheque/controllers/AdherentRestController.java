package com.bibliotheque.controllers;

import com.bibliotheque.entities.Adherent;
import com.bibliotheque.entities.ProfilsAdherent;
import com.bibliotheque.entities.Penalite;
import com.bibliotheque.entities.Abonnement;
import com.bibliotheque.services.AdherentService;
import com.bibliotheque.services.ProfilsAdherentService;
import com.bibliotheque.services.PenaliteService;
import com.bibliotheque.services.AbonnementService;
import com.bibliotheque.repositories.EmpruntRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.*;

@RestController
@RequestMapping("/api/adherent")
public class AdherentRestController {
    @Autowired
    private AdherentService adherentService;
    @Autowired
    private ProfilsAdherentService profilsAdherentService;
    @Autowired
    private PenaliteService penaliteService;
    @Autowired
    private AbonnementService abonnementService;
    @Autowired
    private EmpruntRepository empruntRepository;

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getAdherentDetail(@PathVariable Integer id) {
        Optional<Adherent> adherentOpt = adherentService.findById(id);
        if (adherentOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Adhérent non trouvé"));
        }
        Adherent adherent = adherentOpt.get();
        Optional<ProfilsAdherent> profilOpt = profilsAdherentService.repository.findById(adherent.getIdProfil());
        String nomProfil = profilOpt.map(ProfilsAdherent::getNomProfil).orElse(null);
        int quotaTotal = profilOpt.map(ProfilsAdherent::getQuotaEmpruntsSimultanes).orElse(0);

        long empruntsActifs = empruntRepository.countByAdherentAndStatutActuel(adherent, List.of("En cours", "Retard"));
        int quotaDispo = quotaTotal - (int)empruntsActifs;

        List<Penalite> penalites = penaliteService.repository.findByAdherentId(adherent.getId());
        List<Map<String, Object>> penalitesDto = new ArrayList<>();
        for (Penalite p : penalites) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", p.getId());
            map.put("raison", p.getRaison());
            map.put("dateDebut", p.getDateDebut());
            map.put("jour", p.getJour());
            penalitesDto.add(map);
        }
        // Abonnement actif (DTO)
        Optional<Abonnement> abonnementOpt = abonnementService.repository.findAll().stream()
            .filter(a -> a.getAdherent().getId().equals(adherent.getId()))
            .filter(a -> a.getDateFin().isAfter(java.time.LocalDate.now()) || a.getDateFin().isEqual(java.time.LocalDate.now()))
            .findFirst();
        Map<String, Object> abonnementDto = null;
        if (abonnementOpt.isPresent()) {
            Abonnement a = abonnementOpt.get();
            abonnementDto = new HashMap<>();
            abonnementDto.put("id", a.getId());
            abonnementDto.put("dateDebut", a.getDateDebut());
            abonnementDto.put("dateFin", a.getDateFin());
        }
        Map<String, Object> result = new HashMap<>();
        result.put("id", adherent.getId());
        result.put("nom", adherent.getNom());
        result.put("prenom", adherent.getPrenom());
        result.put("profil", nomProfil);
        result.put("quotaTotal", quotaTotal);
        result.put("quotaDispo", quotaDispo);
        result.put("penalites", penalitesDto);
        result.put("abonnementActif", abonnementDto);
        return ResponseEntity.ok(result);
    }
}
