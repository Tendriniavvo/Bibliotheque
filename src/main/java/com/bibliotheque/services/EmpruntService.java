package com.bibliotheque.services;

import com.bibliotheque.entities.*;
import com.bibliotheque.repositories.*;
import com.bibliotheque.exceptions.EmpruntException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
        // Vérification de l'adhérent
        Adherent adherent = adherentRepository.findById(emprunt.getAdherent().getId())
                .orElseThrow(() -> new EmpruntException("Adhérent introuvable"));

        // Vérification des pénalités actives
        LocalDate dateEmprunt = emprunt.getDateEmprunt();
        List<Penalite> penalites = penaliteRepository.findActivePenalitesByAdherent(adherent.getId(), dateEmprunt);
        for (Penalite penalite : penalites) {
            LocalDate debut = penalite.getDateDebut();
            LocalDate fin = debut.plusDays(penalite.getJour() - 1);
            if ((dateEmprunt.isEqual(debut) || dateEmprunt.isAfter(debut)) && dateEmprunt.isBefore(fin.plusDays(1))) {
                throw new EmpruntException("L'adhérent a une pénalité en cours à cette date et ne peut pas emprunter.");
            }
        }

        // Vérification du profil et du quota d'emprunts
        ProfilsAdherent profil = profilAdherentRepository.findById(adherent.getIdProfil())
                .orElseThrow(() -> new EmpruntException("Le profil de l'adhérent n'existe pas."));
        // On compte dynamiquement le nombre d'emprunts en cours pour cet adhérent (statut réel)
        long empruntsEnCours = empruntRepository.countByAdherentAndStatutActuel(adherent, List.of("En cours", "Retard"));
        int quotaMax = profil.getQuotaEmpruntsSimultanes();
        int quotaRestant = quotaMax - (int) empruntsEnCours;
        if (quotaRestant <= 0) {
            throw new EmpruntException("L'adhérent a atteint ou dépassé son quota d'emprunts simultanés (" + quotaMax + "). Quota restant : 0");
        }

        // Vérification du quota de jours de prêt
        int quotaJoursPret = profil.getQuotaJoursPret();
        long dureeEmprunt = java.time.temporal.ChronoUnit.DAYS.between(emprunt.getDateEmprunt(), emprunt.getDateRetourPrevue()) + 1;
        if (dureeEmprunt > quotaJoursPret) {
            throw new EmpruntException(
                "La durée de l'emprunt demandée (" + dureeEmprunt + " jours) dépasse le quota autorisé par votre profil (" + quotaJoursPret + " jours)."
            );
        }

        // Vérification de l'exemplaire
        Exemplaire exemplaire = exemplaireRepository.findById(emprunt.getExemplaire().getId())
                .orElseThrow(() -> new EmpruntException("L'exemplaire spécifié n'existe pas."));
        // Vérification de la disponibilité réelle de l'exemplaire
        if (exemplaire.getQuantite() <= 0) {
            throw new EmpruntException("Aucun exemplaire disponible pour cet emprunt.");
        }

        // Vérification de l'abonnement
        Optional<Abonnement> abonnementOpt = abonnementRepository.findActiveAbonnementByAdherent(adherent.getId(), dateEmprunt);
        if (abonnementOpt.isEmpty()) {
            throw new EmpruntException("L'adhérent n'a pas d'abonnement actif à la date de l'emprunt.");
        }
        Abonnement abonnement = abonnementOpt.get();
        LocalDate dateRetourPrevue = emprunt.getDateRetourPrevue();
        LocalDate dateFinAbonnement = abonnement.getDateFin();
        if (dateRetourPrevue.isAfter(dateFinAbonnement)) {
            throw new EmpruntException("La date de retour prévue (" + dateRetourPrevue
                    + ") dépasse la fin de l'abonnement (" + dateFinAbonnement + ").");
        }

        // Vérification du type d'emprunt
        TypeEmprunt typeEmprunt = typeEmpruntRepository.findById(emprunt.getTypeEmprunt().getId())
                .orElseThrow(() -> new EmpruntException("Le type d'emprunt spécifié n'existe pas."));

        // Vérification des jours fériés
        if (joursFeriesRepository.existsByDateFerie(dateEmprunt)
                || joursFeriesRepository.existsByDateFerie(dateRetourPrevue)) {
            throw new EmpruntException("L'emprunt ou le retour prévu tombe sur un jour férié.");
        }

        // Vérification des réservations
        List<Reservation> reservations = reservationRepository.findByLivreId(exemplaire.getLivre().getId());
        if (!reservations.isEmpty()
                && reservations.stream().noneMatch(r -> r.getAdherent().getId().equals(adherent.getId()))) {
            throw new EmpruntException("Le livre est réservé par un autre adhérent.");
        }

        // Vérification des dates
        if (emprunt.getDateEmprunt() == null) {
            throw new EmpruntException("La date d'emprunt est invalide.");
        }
        if (emprunt.getDateRetourPrevue() == null || !emprunt.getDateRetourPrevue().isAfter(emprunt.getDateEmprunt())) {
            throw new EmpruntException("La date de retour prévue doit être postérieure à la date d'emprunt.");
        }

        // Décrémentation de la quantité de l'exemplaire
        exemplaire.setQuantite(exemplaire.getQuantite() - 1);
        exemplaireRepository.save(exemplaire);

        // Enregistrement de l'emprunt
        emprunt.setAdherent(adherent);
        Emprunt savedEmprunt = empruntRepository.save(emprunt);

        // Création du mouvement d'emprunt avec le statut "En cours"
        StatutEmprunt statutEnCours = statutEmpruntRepository.findByCodeStatut("En cours")
                .orElseGet(() -> {
                    StatutEmprunt newStatut = new StatutEmprunt();
                    newStatut.setCodeStatut("En cours");
                    return statutEmpruntRepository.save(newStatut);
                });
        MvtEmprunt mvt = new MvtEmprunt();
        mvt.setEmprunt(savedEmprunt);
        mvt.setStatutNouveau(statutEnCours);
        mvt.setDateMouvement(emprunt.getDateEmprunt());
        mvtEmpruntRepository.save(mvt);

        return savedEmprunt;
    }

    public String getLastStatutForEmprunt(Integer empruntId) {
        return mvtEmpruntRepository.findTopByEmpruntIdOrderByDateMouvementDesc(empruntId)
                .map(mvt -> mvt.getStatutNouveau().getCodeStatut())
                .orElse("Inconnu");
    }

    @Transactional
    public Emprunt rendreEmprunt(Integer empruntId, LocalDate dateRetourEffective) throws EmpruntException {
        // 1. Vérifier que l'emprunt existe
        Emprunt emprunt = empruntRepository.findById(empruntId)
                .orElseThrow(() -> new EmpruntException("Emprunt introuvable avec l'ID : " + empruntId));

        // 2. Vérifier le statut actuel de l'emprunt
        String statutActuel = getLastStatutForEmprunt(empruntId);
        // if (!"En cours".equalsIgnoreCase(statutActuel) && !"Retard".equalsIgnoreCase(statutActuel)) {
        //     throw new EmpruntException(
        //             "L'emprunt n'est pas dans un état permettant le retour. Statut actuel : " + statutActuel);
        // }

        // // 3. Vérifier que la date de retour est valide
        // if (dateRetourEffective == null) {
        //     throw new EmpruntException("La date de retour effective ne peut pas être nulle");
        // }

        // if (dateRetourEffective.isBefore(emprunt.getDateEmprunt())) {
        //     throw new EmpruntException("La date de retour ne peut pas être antérieure à la date d'emprunt");
        // }

        // // 4. Vérifier que ce n'est pas un jour férié
        // if (joursFeriesRepository.existsByDateFerie(dateRetourEffective)) {
        //     throw new EmpruntException("Le retour ne peut pas être effectué un jour férié");
        // }

        // 5. Récupérer l'exemplaire pour remettre à jour la quantité
        Exemplaire exemplaire = exemplaireRepository.findById(emprunt.getExemplaire().getId())
                .orElseThrow(() -> new EmpruntException("L'exemplaire associé à l'emprunt n'existe plus"));

        // 6. Récupérer l'adhérent et son profil pour gérer le quota
        Adherent adherent = emprunt.getAdherent();
        ProfilsAdherent profil = profilAdherentRepository.findById(adherent.getIdProfil())
                .orElseThrow(() -> new EmpruntException("Profil de l'adhérent introuvable"));
        int quotaMax = profil.getQuotaEmpruntsSimultanes();

        // 7. Vérifier s'il y a un retard et gérer les pénalités
        boolean enRetard = dateRetourEffective.isAfter(emprunt.getDateRetourPrevue());
        if (enRetard) {
            // Calculer la durée du retard
            long joursRetard = java.time.temporal.ChronoUnit.DAYS.between(emprunt.getDateRetourPrevue(),
                    dateRetourEffective);

            // Créer une pénalité
            Penalite penalite = new Penalite();
            penalite.setEmprunt(emprunt);
            penalite.setAdherent(adherent);
            penalite.setDateDebut(dateRetourEffective);
            penalite.setJour((int) joursRetard);
            penalite.setRaison("Retard de " + joursRetard + " jour(s) pour l'emprunt ID " + empruntId);
            penaliteRepository.save(penalite);
        }

        // 8. Mettre à jour la quantité d'exemplaires (rendre l'exemplaire disponible)
        exemplaire.setQuantite(exemplaire.getQuantite() + 1);
        exemplaireRepository.save(exemplaire);

        // 9. Enregistrer le mouvement d'emprunt avec statut 'Rendu'
        StatutEmprunt statutRendu = statutEmpruntRepository.findByCodeStatut("Rendu")
                .orElseThrow(() -> new EmpruntException("Le statut 'Rendu' n'existe pas dans la base."));

        MvtEmprunt mvt = new MvtEmprunt();
        mvt.setEmprunt(emprunt);
        System.out.println("l'id de l'emprunt : " + emprunt.getId() );
        mvt.setStatutNouveau(statutRendu);
        mvt.setDateMouvement(dateRetourEffective); // Utiliser directement LocalDate
        mvtEmpruntRepository.save(mvt);

        // 10. Vérifier le quota d'emprunts simultanés
        long empruntsEnCours = empruntRepository.countByAdherentAndStatutActuel(adherent,
                List.of("En cours", "Retard"));
        if (empruntsEnCours >= quotaMax) {
            // log.info("Adhérent {} a atteint son quota d'emprunts simultanés ({}).", adherent.getId(), quotaMax);
        }

        // 11. Gérer les réservations pour ce livre
        List<Reservation> reservations = reservationRepository.findByLivreId(exemplaire.getLivre().getId());
        if (!reservations.isEmpty()) {
            Reservation premiereReservation = reservations.get(0);
            // log.info("Livre {} est maintenant disponible pour la réservation ID {}.", exemplaire.getLivre().getId(),
                    // premiereReservation.getId());
            // Option : Mettre à jour le statut de la réservation si nécessaire
        }

        return emprunt;
    }

    // Méthode utilitaire pour vérifier si un emprunt peut être rendu
    public boolean peutEtreRendu(Integer empruntId) {
        try {
            String statutActuel = getLastStatutForEmprunt(empruntId);
            return "En cours".equalsIgnoreCase(statutActuel) || "Retard".equalsIgnoreCase(statutActuel);
        } catch (Exception e) {
            return false;
        }
    }

    // Méthode pour obtenir tous les emprunts en cours d'un adhérent
    public List<Emprunt> getEmpruntsEnCours(Integer adherentId) {
        List<Emprunt> emprunts = empruntRepository.findByAdherentId(adherentId);
        return emprunts.stream()
                .filter(emprunt -> {
                    String statut = getLastStatutForEmprunt(emprunt.getId());
                    return "En cours".equalsIgnoreCase(statut) || "Retard".equalsIgnoreCase(statut);
                })
                .collect(java.util.stream.Collectors.toList());
    }

}