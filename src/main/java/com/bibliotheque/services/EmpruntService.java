package com.bibliotheque.services;

import com.bibliotheque.entities.*;
import com.bibliotheque.repositories.*;
import com.bibliotheque.exceptions.EmpruntException;
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
    private final StatutEmpruntRepository statutEmpruntRepository;

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
            MvtEmpruntRepository mvtEmpruntRepository,
            StatutEmpruntRepository statutEmpruntRepository) {
        this.statutEmpruntRepository = statutEmpruntRepository;
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

    public List<Emprunt> findAll() {
        return empruntRepository.findAll();
    }

    @Transactional
    public Emprunt save(Emprunt emprunt) throws EmpruntException {
        // 1. Recharge l'adhérent pour avoir le profil complet
        Adherent adherent = adherentRepository.findById(emprunt.getAdherent().getId())
                .orElseThrow(() -> new EmpruntException("Adhérent introuvable"));

        // 2. Vérification des pénalités en cours
        LocalDate dateEmprunt = emprunt.getDateEmprunt().atZone(ZoneId.systemDefault()).toLocalDate();
        List<Penalite> penalites = penaliteRepository.findActivePenalitesByAdherent(adherent.getId(), dateEmprunt);
        for (Penalite penalite : penalites) {
            LocalDate debut = penalite.getDateDebut();
            LocalDate fin = debut.plusDays(penalite.getJour() - 1);
            if ((dateEmprunt.isEqual(debut) || dateEmprunt.isAfter(debut)) && dateEmprunt.isBefore(fin.plusDays(1))) {
                throw new EmpruntException("L'adhérent a une pénalité en cours à cette date et ne peut pas emprunter.");
            }
        }

        // 3. Vérification du quota d'emprunts simultanés
        ProfilsAdherent profil = profilAdherentRepository.findById(adherent.getIdProfil())
                .orElseThrow(() -> new EmpruntException("Le profil de l'adhérent n'existe pas."));
        long empruntsActifs = empruntRepository.countByIdAdherentAndStatutEnCours(adherent.getId(), emprunt.getDateEmprunt());
        if (empruntsActifs >= profil.getQuotaEmpruntsSimultanes()) {
            throw new EmpruntException("L'adhérent a déjà atteint son quota d'emprunts simultanés.");
        }

        // 4. Vérification de la disponibilité de l'exemplaire
        Exemplaire exemplaire = exemplaireRepository.findById(emprunt.getExemplaire().getId())
                .orElseThrow(() -> new EmpruntException("L'exemplaire spécifié n'existe pas."));
        int quantiteTotale = exemplaire.getQuantite();
        List<Emprunt> empruntsExemplaire = empruntRepository.findByExemplaireId(exemplaire.getId());
        int disponible = quantiteTotale;
        for (Emprunt e : empruntsExemplaire) {
            String statut = getLastStatutForEmprunt(e.getId());
            if ("En cours".equalsIgnoreCase(statut) || "Retard".equalsIgnoreCase(statut)) {
                LocalDateTime debutExist = e.getDateEmprunt().atZone(ZoneId.systemDefault()).toLocalDateTime();
                LocalDateTime finExist = e.getDateRetourPrevue().atZone(ZoneId.systemDefault()).toLocalDateTime();
                LocalDateTime debutNouveau = emprunt.getDateEmprunt().atZone(ZoneId.systemDefault()).toLocalDateTime();
                LocalDateTime finNouveau = emprunt.getDateRetourPrevue().atZone(ZoneId.systemDefault()).toLocalDateTime();
                boolean chevauche = !finNouveau.isBefore(debutExist) && !debutNouveau.isAfter(finExist);
                if (chevauche) {
                    throw new EmpruntException("Un autre emprunt pour cet exemplaire chevauche la période demandée (statut 'En cours' ou 'Retard').");
                }
            }
            if ("En cours".equalsIgnoreCase(statut)) {
                disponible -= 1;
            } else if ("Rendu".equalsIgnoreCase(statut)) {
                disponible += 1;
            }
        }
        if (disponible <= 0) {
            throw new EmpruntException("Aucun exemplaire disponible pour cet emprunt.");
        }

        // 5. Vérification de l'abonnement actif
        boolean hasActiveAbonnement = abonnementRepository.findActiveAbonnementByAdherent(adherent.getId(), dateEmprunt).isPresent();
        if (!hasActiveAbonnement) {
            throw new EmpruntException("L'adhérent n'a pas d'abonnement actif à la date de l'emprunt.");
        }

        // 6. Vérification de l'existence du type d'emprunt
        TypeEmprunt typeEmprunt = typeEmpruntRepository.findById(emprunt.getTypeEmprunt().getId())
                .orElseThrow(() -> new EmpruntException("Le type d'emprunt spécifié n'existe pas."));

        // 7. Vérification des jours fériés
        LocalDate dateRetourPrevue = emprunt.getDateRetourPrevue().atZone(ZoneId.systemDefault()).toLocalDate();
        if (joursFeriesRepository.existsByDateFerie(dateEmprunt) || joursFeriesRepository.existsByDateFerie(dateRetourPrevue)) {
            throw new EmpruntException("L'emprunt ou le retour prévu tombe sur un jour férié.");
        }

        // 8. Vérification des réservations actives
        List<Reservation> reservations = reservationRepository.findByLivreIdAndStatutId(exemplaire.getLivre().getId(), 1);
        if (!reservations.isEmpty() && reservations.stream().noneMatch(r -> r.getAdherent().getId().equals(adherent.getId()))) {
            throw new EmpruntException("Le livre est réservé par un autre adhérent.");
        }

        // 9. Vérification de la validité des dates
        if (emprunt.getDateEmprunt() == null) {
            throw new EmpruntException("La date d'emprunt est invalide.");
        }
        if (emprunt.getDateRetourPrevue() == null || !emprunt.getDateRetourPrevue().isAfter(emprunt.getDateEmprunt())) {
            throw new EmpruntException("La date de retour prévue doit être postérieure à la date d'emprunt.");
        }

        // 10. Mettre à jour la quantité d'exemplaires
        exemplaire.setQuantite(exemplaire.getQuantite() - 1);
        exemplaireRepository.save(exemplaire);

        // 11. Enregistrer l'emprunt
        emprunt.setAdherent(adherent); // Assurer que l'adhérent complet est associé
        Emprunt savedEmprunt = empruntRepository.save(emprunt);

        // 12. Enregistrer le mouvement d'emprunt avec statut 'En cours'
        StatutEmprunt statutEnCours = statutEmpruntRepository.findByCodeStatut("En cours")
                .orElseThrow(() -> new EmpruntException("Statut 'En cours' introuvable"));
        MvtEmprunt mvt = new MvtEmprunt();
        mvt.setEmprunt(savedEmprunt);
        mvt.setStatutNouveau(statutEnCours);
        mvt.setDateMouvement(emprunt.getDateEmprunt());
        mvtEmpruntRepository.save(mvt);

        return savedEmprunt;
    }

    // Méthode utilitaire pour obtenir le dernier statut d'un emprunt


    // Méthode utilitaire pour calculer l'âge (conservée mais non utilisée ici)
    private int calculateAge(LocalDate dateNaissance, Instant referenceDate) {
        LocalDate refDate = referenceDate.atZone(ZoneId.systemDefault()).toLocalDate();
        return java.time.Period.between(dateNaissance, refDate).getYears();
    }

    public String getLastStatutForEmprunt(Integer empruntId) {
        return mvtEmpruntRepository.findTopByEmpruntIdOrderByDateMouvementDesc(empruntId)
            .map(mvt -> mvt.getStatutNouveau().getCodeStatut())
            .orElse("Inconnu");
    }
}