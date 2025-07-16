package com.bibliotheque.services;

import com.bibliotheque.entities.*;
import com.bibliotheque.exceptions.EmpruntException;
import com.bibliotheque.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class ProlongementService {

    private final ProlongementRepository prolongementRepository;
    private final EmpruntRepository empruntRepository;
    private final AdherentRepository adherentRepository;
    private final ProfilsAdherentRepository profilAdherentRepository;
    private final PenaliteRepository penaliteRepository;
    private final AbonnementRepository abonnementRepository;
    private final JourFerieRepository joursFeriesRepository;
    private final ReservationRepository reservationRepository;
    private final ExemplaireRepository exemplaireRepository;
    private final MvtEmpruntRepository mvtEmpruntRepository;
    private final StatutEmpruntRepository statutEmpruntRepository;
    private final StatutProlongementRepository statutProlongementRepository;
    private final MvtProlongementRepository mvtProlongementRepository;

    @Autowired
    public ProlongementService(
            ProlongementRepository prolongementRepository,
            EmpruntRepository empruntRepository,
            AdherentRepository adherentRepository,
            ProfilsAdherentRepository profilAdherentRepository,
            PenaliteRepository penaliteRepository,
            AbonnementRepository abonnementRepository,
            JourFerieRepository joursFeriesRepository,
            ReservationRepository reservationRepository,
            ExemplaireRepository exemplaireRepository,
            MvtEmpruntRepository mvtEmpruntRepository,
            StatutEmpruntRepository statutEmpruntRepository,
            StatutProlongementRepository statutProlongementRepository,
            MvtProlongementRepository mvtProlongementRepository) {
        this.prolongementRepository = prolongementRepository;
        this.empruntRepository = empruntRepository;
        this.adherentRepository = adherentRepository;
        this.profilAdherentRepository = profilAdherentRepository;
        this.penaliteRepository = penaliteRepository;
        this.abonnementRepository = abonnementRepository;
        this.joursFeriesRepository = joursFeriesRepository;
        this.reservationRepository = reservationRepository;
        this.exemplaireRepository = exemplaireRepository;
        this.mvtEmpruntRepository = mvtEmpruntRepository;
        this.statutEmpruntRepository = statutEmpruntRepository;
        this.statutProlongementRepository = statutProlongementRepository;
        this.mvtProlongementRepository = mvtProlongementRepository;
    }

    public List<Prolongement> getAll() {
        return prolongementRepository.findAll();
    }

    public Optional<Prolongement> findById(Integer id) {
        return prolongementRepository.findById(id);
    }

    @Transactional
    public Prolongement save(Prolongement prolongement) throws EmpruntException {
        // 1. Vérifier l'existence de l'emprunt
        Emprunt emprunt = empruntRepository.findById(prolongement.getEmprunt().getId())
                .orElseThrow(() -> new EmpruntException("L'emprunt spécifié n'existe pas."));

        // 2. Recharge l'adhérent pour avoir le profil complet
        Adherent adherent = adherentRepository.findById(emprunt.getAdherent().getId())
                .orElseThrow(() -> new EmpruntException("Adhérent introuvable"));

        // 3. Vérification des pénalités en cours
        LocalDate dateProlongement = prolongement.getDateFin();
        if (dateProlongement == null) {
            throw new EmpruntException("La date de prolongement est requise.");
        }
        List<Penalite> penalites = penaliteRepository.findActivePenalitesByAdherent(adherent.getId(), dateProlongement);
        for (Penalite penalite : penalites) {
            LocalDate debut = penalite.getDateDebut();
            LocalDate fin = debut.plusDays(penalite.getJour());
            if ((dateProlongement.isEqual(debut) || dateProlongement.isAfter(debut))
                    && dateProlongement.isBefore(fin.plusDays(1))) {
                throw new EmpruntException(
                        "L'adhérent a une pénalité en cours à la date de prolongement et ne peut pas prolonger.");
            }
        }

        // 4. Vérification de la validité de la nouvelle date de retour
        LocalDate newDateRetourPrevue = prolongement.getDateFin();
        if (newDateRetourPrevue == null || !newDateRetourPrevue.isAfter(emprunt.getDateRetourPrevue())) {
            throw new EmpruntException(
                    "La nouvelle date de retour prévue doit être postérieure à la date de retour actuelle.");
        }

        // 5. Vérification de l'abonnement actif pour la nouvelle période
        Optional<Abonnement> abonnementOpt = abonnementRepository.findActiveAbonnementByAdherent(adherent.getId(),
                dateProlongement);
        if (abonnementOpt.isEmpty()) {
            throw new EmpruntException("L'adhérent n'a pas d'abonnement actif à la date de prolongement.");
        }
        Abonnement abonnement = abonnementOpt.get();
        LocalDate dateFinAbonnement = abonnement.getDateFin();
        if (newDateRetourPrevue.isAfter(dateFinAbonnement)) {
            throw new EmpruntException("La date de prolongement (" + newDateRetourPrevue
                    + ") dépasse la fin de l'abonnement (" + dateFinAbonnement + ").");
        }

        // 6. Vérification des jours fériés
        if (joursFeriesRepository.existsByDateFerie(newDateRetourPrevue)) {
            throw new EmpruntException("La nouvelle date de retour tombe sur un jour férié.");
        }

        // 7. Vérification des réservations actives
        Exemplaire exemplaire = exemplaireRepository.findById(emprunt.getExemplaire().getId())
                .orElseThrow(() -> new EmpruntException("L'exemplaire spécifié n'existe pas."));
        List<Reservation> reservations = reservationRepository.findByLivreId(exemplaire.getLivre().getId());
        if (!reservations.isEmpty()
                && reservations.stream().noneMatch(r -> r.getAdherent().getId().equals(adherent.getId()))) {
            throw new EmpruntException("Le livre est réservé par un autre adhérent.");
        }

        // 8. Vérification de la disponibilité de l'exemplaire pour la période prolongée
        List<Emprunt> empruntsExemplaire = empruntRepository.findByExemplaireId(exemplaire.getId());
        for (Emprunt e : empruntsExemplaire) {
            if (!e.getId().equals(emprunt.getId())) { // Exclure l'emprunt actuel
                String statut = getLastStatutForEmprunt(e.getId());
                if ("En cours".equalsIgnoreCase(statut) || "Retard".equalsIgnoreCase(statut)) {
                    LocalDate debutExist = e.getDateEmprunt();
                    LocalDate finExist = e.getDateRetourPrevue();
                    LocalDate debutNouveau = emprunt.getDateEmprunt();
                    LocalDate finNouveau = prolongement.getDateFin();
                    boolean chevauche = !finNouveau.isBefore(debutExist) && !debutNouveau.isAfter(finExist);
                    if (chevauche) {
                        throw new EmpruntException(
                                "Un autre emprunt pour cet exemplaire chevauche la période prolongée.");
                    }
                }
            }
        }

        // Vérification du quota de prolongements
        ProfilsAdherent profil = profilAdherentRepository.findById(adherent.getId())
                .orElseThrow(() -> new EmpruntException("Profil d'adhérent introuvable."));
        int quotaProlongement = profil.getQuotaProlongementPret();
        long nbProlongementsActifs = prolongementRepository.countActiveProlongementsByEmpruntId(emprunt.getId());
        if (nbProlongementsActifs >= quotaProlongement) {
            throw new EmpruntException("Vous avez atteint le quota maximum de prolongements actifs autorisés (" + quotaProlongement + ").");
        }

        // 9. Mettre à jour la date de retour prévue de l'emprunt

        // 10. Enregistrer le mouvement d'emprunt avec statut 'Prolongé'
        StatutEmprunt statutProlonge = statutEmpruntRepository.findByCodeStatut("Prolonge")
                .orElseGet(() -> {
                    StatutEmprunt newStatut = new StatutEmprunt();
                    newStatut.setCodeStatut("Prolonge");

                    return statutEmpruntRepository.save(newStatut);
                });
        MvtEmprunt mvt = new MvtEmprunt();
        mvt.setEmprunt(emprunt);
        mvt.setStatutNouveau(statutProlonge);
        mvt.setDateMouvement(prolongement.getDateProlongement());
        mvtEmpruntRepository.save(mvt);

        // 11. Enregistrer le prolongement
        Prolongement savedProlongement = prolongementRepository.save(prolongement);

        // Insérer un mouvement 'En attente' lors de la création
        StatutProlongement statutEnAttente = statutProlongementRepository.findByCodeStatut("En attente")
            .orElseGet(() -> {
                StatutProlongement newStatut = new StatutProlongement();
                newStatut.setCodeStatut("En attente");
                return statutProlongementRepository.save(newStatut);
            });
        MvtProlongement mvtEnAttente = new MvtProlongement();
        mvtEnAttente.setProlongement(savedProlongement);
        mvtEnAttente.setStatutNouveau(statutEnAttente);
        mvtEnAttente.setDateMouvement(prolongement.getDateProlongement());
        mvtProlongementRepository.save(mvtEnAttente);

        return savedProlongement;
    }

    @Transactional
    public void validerProlongement(Integer prolongementId) throws EmpruntException {
        Prolongement prolongement = prolongementRepository.findById(prolongementId)
            .orElseThrow(() -> new EmpruntException("Prolongement introuvable."));

        StatutProlongement statut = statutProlongementRepository.findByCodeStatut("Validee")
            .orElseThrow(() -> new EmpruntException("Statut 'Validee' introuvable."));

        // Vérifier si le dernier mouvement est déjà 'Validee' pour éviter de dupliquer
        List<MvtProlongement> mouvements = mvtProlongementRepository.findAll();
        boolean dejaValide = mouvements.stream().anyMatch(m -> m.getProlongement().getId().equals(prolongementId) && m.getStatutNouveau().getCodeStatut().equalsIgnoreCase("Validee"));
        if (!dejaValide) {
            // Mettre à jour la date de retour prévue de l'emprunt lors de la validation
            Emprunt emprunt = prolongement.getEmprunt();
            emprunt.setDateRetourPrevue(prolongement.getDateFin());
            empruntRepository.save(emprunt);
        }

        // Insérer un mouvement à chaque validation
        MvtProlongement mvt = new MvtProlongement();
        mvt.setProlongement(prolongement);
        mvt.setStatutNouveau(statut);
        mvt.setDateMouvement(LocalDate.now());
        mvtProlongementRepository.save(mvt);
    }

    public List<MvtProlongement> getMouvementsForProlongement(Integer prolongementId) {
        return mvtProlongementRepository.findByProlongementIdOrderByDateMouvementDesc(prolongementId);
    }

    // Méthode utilitaire pour obtenir le dernier statut d'un emprunt
    private String getLastStatutForEmprunt(Integer empruntId) {
        Optional<MvtEmprunt> dernierMvt = mvtEmpruntRepository.findTopByEmpruntIdOrderByDateMouvementDesc(empruntId);
        return dernierMvt.map(mvt -> mvt.getStatutNouveau().getCodeStatut()).orElse("Inconnu");
    }
}