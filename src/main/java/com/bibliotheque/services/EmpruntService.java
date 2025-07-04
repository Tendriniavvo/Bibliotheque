package com.bibliotheque.services;

import com.bibliotheque.entities.*;
import com.bibliotheque.repositories.*;
import com.bibliotheque.exceptions.EmpruntException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public long countActiveEmpruntsByLivreAndDate(Integer livreId, LocalDate date) {
        return empruntRepository.countActiveEmpruntsByLivreAndDate(livreId, date);
    }

    @Transactional
    public Emprunt save(Emprunt emprunt) throws EmpruntException {
        // 1. Recharge l'adhérent pour avoir le profil complet
        Adherent adherent = adherentRepository.findById(emprunt.getAdherent().getId())
                .orElseThrow(() -> new EmpruntException("Adhérent introuvable"));

        // 2. Vérification des pénalités en cours
        LocalDate dateEmprunt = emprunt.getDateEmprunt();
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
                LocalDate debutExist = e.getDateEmprunt();
                LocalDate finExist = e.getDateRetourPrevue();
                LocalDate debutNouveau = emprunt.getDateEmprunt();
                LocalDate finNouveau = emprunt.getDateRetourPrevue();
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

        // 5. Vérification de l'abonnement actif pour toute la période de l'emprunt
        LocalDate dateRetourPrevue = emprunt.getDateRetourPrevue();
        Optional<Abonnement> abonnementOpt = abonnementRepository.findActiveAbonnementByAdherent(adherent.getId(), dateEmprunt);
        if (abonnementOpt.isEmpty()) {
            throw new EmpruntException("L'adhérent n'a pas d'abonnement actif à la date de l'emprunt.");
        }
        Abonnement abonnement = abonnementOpt.get();
        LocalDate dateFinAbonnement = abonnement.getDateFin();
        if (dateRetourPrevue.isAfter(dateFinAbonnement)) {
            throw new EmpruntException("La date de retour prévue (" + dateRetourPrevue + ") dépasse la fin de l'abonnement (" + dateFinAbonnement + ").");
        }

        // 6. Vérification de l'existence du type d'emprunt
        TypeEmprunt typeEmprunt = typeEmpruntRepository.findById(emprunt.getTypeEmprunt().getId())
                .orElseThrow(() -> new EmpruntException("Le type d'emprunt spécifié n'existe pas."));

        // 7. Vérification des jours fériés
        if (joursFeriesRepository.existsByDateFerie(dateEmprunt) || joursFeriesRepository.existsByDateFerie(dateRetourPrevue)) {
            throw new EmpruntException("L'emprunt ou le retour prévu tombe sur un jour férié.");
        }

        // 8. Vérification des réservations actives
        List<Reservation> reservations = reservationRepository.findByLivreId(exemplaire.getLivre().getId());
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
                .orElseGet(() -> {
                    StatutEmprunt newStatut = new StatutEmprunt();
                    newStatut.setCodeStatut("En cours");
                    newStatut.setCodeStatut("Emprunt en cours"); // Assurez-vous que ce setter existe
                    return statutEmpruntRepository.save(newStatut);
                });
        MvtEmprunt mvt = new MvtEmprunt();
        mvt.setEmprunt(savedEmprunt);
        mvt.setStatutNouveau(statutEnCours);
        mvt.setDateMouvement(emprunt.getDateEmprunt()); // Utilise LocalDateTime
        mvtEmpruntRepository.save(mvt);

        return savedEmprunt;
    }

    public String getLastStatutForEmprunt(Integer empruntId) {
        return mvtEmpruntRepository.findTopByEmpruntIdOrderByDateMouvementDesc(empruntId)
                .map(mvt -> mvt.getStatutNouveau().getCodeStatut())
                .orElse("Inconnu");
    }
}