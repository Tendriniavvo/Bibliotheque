
package com.bibliotheque.services;

import com.bibliotheque.entities.*;
import com.bibliotheque.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class EmpruntService {

    private final EmpruntRepository empruntRepository;
    private final ExemplaireRepository exemplaireRepository;
    private final AdherentRepository adherentRepository;
    private final TypeEmpruntRepository typeEmpruntRepository;
    private final AbonnementRepository abonnementRepository;
    private final ProfilsAdherentRepository profilAdherentRepository;
    private final DroitEmpruntSpecifiqueRepository droitsEmpruntSpecifiquesRepository;
    private final PenaliteRepository penaliteRepository;
    private final JourFerieRepository joursFeriesRepository;
    private final ReservationRepository reservationRepository;
    private final MvtEmpruntRepository mvtEmpruntRepository;

    @Autowired
    public EmpruntService(
            EmpruntRepository empruntRepository,
            ExemplaireRepository exemplaireRepository,
            AdherentRepository adherentRepository,
            TypeEmpruntRepository typeEmpruntRepository,
            AbonnementRepository abonnementRepository,
            ProfilsAdherentRepository profilAdherentRepository,
            DroitEmpruntSpecifiqueRepository droitsEmpruntSpecifiquesRepository,
            PenaliteRepository penaliteRepository,
            JourFerieRepository joursFeriesRepository,
            ReservationRepository reservationRepository,
            MvtEmpruntRepository mvtEmpruntRepository) {
        this.empruntRepository = empruntRepository;
        this.exemplaireRepository = exemplaireRepository;
        this.adherentRepository = adherentRepository;
        this.typeEmpruntRepository = typeEmpruntRepository;
        this.abonnementRepository = abonnementRepository;
        this.profilAdherentRepository = profilAdherentRepository;
        this.droitsEmpruntSpecifiquesRepository = droitsEmpruntSpecifiquesRepository;
        this.penaliteRepository = penaliteRepository;
        this.joursFeriesRepository = joursFeriesRepository;
        this.reservationRepository = reservationRepository;
        this.mvtEmpruntRepository = mvtEmpruntRepository;
    }

    public Optional<Emprunt> findById(Integer id) {
        return empruntRepository.findById(id);
    }

    public List<Emprunt> getAll() {
        return empruntRepository.findAll();
    }

    @Transactional
    public Emprunt save(Emprunt emprunt) throws EmpruntException {
        // 1. Vérifier l'existence de l'exemplaire
        Exemplaire exemplaire = exemplaireRepository.findById(emprunt.getExemplaire().getId())
                .orElseThrow(() -> new EmpruntException("L'exemplaire spécifié n'existe pas."));
        if (exemplaire.getQuantite() <= 0) {
            throw new EmpruntException("Aucun exemplaire disponible pour ce livre.");
        }

        // 2. Vérifier l'existence de l'adhérent
        Adherent adherent = adherentRepository.findById(emprunt.getAdherent().getId())
                .orElseThrow(() -> new EmpruntException("L'adhérent spécifié n'existe pas."));

        // 3. Vérifier l'existence du type d'emprunt
        TypeEmprunt typeEmprunt = typeEmpruntRepository.findById(emprunt.getTypeEmprunt().getId())
                .orElseThrow(() -> new EmpruntException("Le type d'emprunt spécifié n'existe pas."));

        // 4. Vérifier le quota d'emprunts simultanés
        ProfilAdherent profil = profilAdherentRepository.findById(adherent.getIdProfil())
                .orElseThrow(() -> new EmpruntException("Le profil de l'adhérent n'existe pas."));
        long empruntsEnCours = empruntRepository.countByIdAdherentAndStatutEnCours(
                adherent.getIdAdherent(), LocalDateTime.now());
        if (empruntsEnCours >= profil.getQuotaEmpruntsSimultanes()) {
            throw new EmpruntException("L'adhérent a atteint son quota d'emprunts simultanés.");
        }

        // 5. Vérifier l'abonnement actif
        boolean hasActiveAbonnement = abonnementRepository.findActiveAbonnementByAdherent(
                adherent.getIdAdherent(), LocalDate.now()).isPresent();
        if (!hasActiveAbonnement) {
            throw new EmpruntException("L'adhérent n'a pas d'abonnement actif.");
        }

        // 6. Vérifier les droits d'emprunt spécifiques
        Optional<DroitEmpruntSpecifique> droitsOpt = droitsEmpruntSpecifiquesRepository
                .findByLivreIdAndProfilId(exemplaire.getLivre().getId(), adherent.getIdProfil());
        if (droitsOpt.isPresent()) {
            DroitEmpruntSpecifique droits = droitsOpt.get();
            if (typeEmprunt.getNomType().equals("DOMICILE") && !droits.isEmpruntDomicileAutorise()) {
                throw new EmpruntException("L'emprunt à domicile n'est pas autorisé pour ce livre.");
            }
            if (typeEmprunt.getNomType().equals("SUR_PLACE") && !droits.isEmpruntSurplaceAutorise()) {
                throw new EmpruntException("L'emprunt sur place n'est pas autorisé pour ce livre.");
            }
            if (droits.getAge() != null && calculateAge(adherent.getDateNaissance()) < droits.getAge()) {
                throw new EmpruntException("L'adhérent n'a pas l'âge requis pour emprunter ce livre.");
            }
        }

        // 7. Vérifier les pénalités actives
        List<Penalite> penalites = penaliteRepository.findActivePenalitesByAdherent(
                adherent.getIdAdherent(), emprunt.getDateEmprunt().atZone(ZoneId.systemDefault()).toLocalDate());
        if (!penalites.isEmpty()) {
            throw new EmpruntException("L'adhérent a des pénalités actives et ne peut pas emprunter.");
        }

        // 8. Vérifier les jours fériés
        LocalDate dateEmprunt = emprunt.getDateEmprunt().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate dateRetourPrevue = emprunt.getDateRetourPrevue().atZone(ZoneId.systemDefault()).toLocalDate();
        if (joursFeriesRepository.existsByDateFerie(dateEmprunt) ||
                joursFeriesRepository.existsByDateFerie(dateRetourPrevue)) {
            throw new EmpruntException("L'emprunt ou le retour prévu tombe sur un jour férié.");
        }

        // 9. Vérifier les réservations actives
        List<Reservation> reservations = reservationRepository.findByIdLivreAndIdStatut(
                exemplaire.getLivre().getId(), 1);
        if (!reservations.isEmpty() &&
                reservations.stream().noneMatch(r -> r.getIdAdherent() == adherent.getIdAdherent())) {
            throw new EmpruntException("Le livre est réservé par un autre adhérent.");
        }

        // 10. Vérifier la validité des dates
        Instant now = Instant.now();
        if (emprunt.getDateEmprunt() == null || emprunt.getDateEmprunt().isAfter(now)) {
            throw new EmpruntException("La date d'emprunt est invalide ou dans le futur.");
        }
        if (emprunt.getDateRetourPrevue() == null ||
                !emprunt.getDateRetourPrevue().isAfter(emprunt.getDateEmprunt())) {
            throw new EmpruntException("La date de retour prévue doit être postérieure à la date d'emprunt.");
        }

        // 11. Mettre à jour la quantité d'exemplaires
        exemplaire.setQuantite(exemplaire.getQuantite() - 1);
        exemplaireRepository.save(exemplaire);

        // 12. Enregistrer l'emprunt
        Emprunt savedEmprunt = empruntRepository.save(emprunt);

        // 13. Enregistrer le statut initial dans Mvt_Emprunt
        MvtEmprunt mvtEmprunt = new MvtEmprunt();
        mvtEmprunt.setIdEmprunt(savedEmprunt.getIdEmprunt());
        mvtEmprunt.setIdStatutNouveau(1); // Statut initial, par exemple "EN_COURS"
        mvtEmprunt.setDateMouvement(LocalDateTime.now());
        mvtEmpruntRepository.save(mvtEmprunt);

        return savedEmprunt;
    }

    // Méthode utilitaire pour calculer l'âge
    private int calculateAge(LocalDate dateNaissance) {
        return java.time.Period.between(dateNaissance, LocalDate.now()).getYears();
    }
}

// Exception personnalisée
class EmpruntException extends Exception {
    public EmpruntException(String message) {
        super(message);
    }
}
