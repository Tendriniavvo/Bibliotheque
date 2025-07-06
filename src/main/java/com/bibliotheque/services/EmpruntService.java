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
        Adherent adherent = adherentRepository.findById(emprunt.getAdherent().getId())
                .orElseThrow(() -> new EmpruntException("Adhérent introuvable"));

        LocalDate dateEmprunt = emprunt.getDateEmprunt();
        List<Penalite> penalites = penaliteRepository.findActivePenalitesByAdherent(adherent.getId(), dateEmprunt);
        for (Penalite penalite : penalites) {
            LocalDate debut = penalite.getDateDebut();
            LocalDate fin = debut.plusDays(penalite.getJour() - 1);
            if ((dateEmprunt.isEqual(debut) || dateEmprunt.isAfter(debut)) && dateEmprunt.isBefore(fin.plusDays(1))) {
                throw new EmpruntException("L'adhérent a une pénalité en cours à cette date et ne peut pas emprunter.");
            }
        }

        ProfilsAdherent profil = profilAdherentRepository.findById(adherent.getIdProfil())
                .orElseThrow(() -> new EmpruntException("Le profil de l'adhérent n'existe pas."));
        long empruntsActifs = empruntRepository.countByIdAdherentAndStatutEnCours(adherent.getId(),
                emprunt.getDateEmprunt());
        if (empruntsActifs >= profil.getQuotaEmpruntsSimultanes()) {
            throw new EmpruntException("L'adhérent a déjà atteint son quota d'emprunts simultanés.");
        }

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
                    throw new EmpruntException(
                            "Un autre emprunt pour cet exemplaire chevauche la période demandée (statut 'En cours' ou 'Retard').");
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

        LocalDate dateRetourPrevue = emprunt.getDateRetourPrevue();
        Optional<Abonnement> abonnementOpt = abonnementRepository.findActiveAbonnementByAdherent(adherent.getId(),
                dateEmprunt);
        if (abonnementOpt.isEmpty()) {
            throw new EmpruntException("L'adhérent n'a pas d'abonnement actif à la date de l'emprunt.");
        }
        Abonnement abonnement = abonnementOpt.get();
        LocalDate dateFinAbonnement = abonnement.getDateFin();
        if (dateRetourPrevue.isAfter(dateFinAbonnement)) {
            throw new EmpruntException("La date de retour prévue (" + dateRetourPrevue
                    + ") dépasse la fin de l'abonnement (" + dateFinAbonnement + ").");
        }

        TypeEmprunt typeEmprunt = typeEmpruntRepository.findById(emprunt.getTypeEmprunt().getId())
                .orElseThrow(() -> new EmpruntException("Le type d'emprunt spécifié n'existe pas."));

        if (joursFeriesRepository.existsByDateFerie(dateEmprunt)
                || joursFeriesRepository.existsByDateFerie(dateRetourPrevue)) {
            throw new EmpruntException("L'emprunt ou le retour prévu tombe sur un jour férié.");
        }

        List<Reservation> reservations = reservationRepository.findByLivreId(exemplaire.getLivre().getId());
        if (!reservations.isEmpty()
                && reservations.stream().noneMatch(r -> r.getAdherent().getId().equals(adherent.getId()))) {
            throw new EmpruntException("Le livre est réservé par un autre adhérent.");
        }

        if (emprunt.getDateEmprunt() == null) {
            throw new EmpruntException("La date d'emprunt est invalide.");
        }
        if (emprunt.getDateRetourPrevue() == null || !emprunt.getDateRetourPrevue().isAfter(emprunt.getDateEmprunt())) {
            throw new EmpruntException("La date de retour prévue doit être postérieure à la date d'emprunt.");
        }

        exemplaire.setQuantite(exemplaire.getQuantite() - 1);
        exemplaireRepository.save(exemplaire);

        emprunt.setAdherent(adherent); 
        Emprunt savedEmprunt = empruntRepository.save(emprunt);

        StatutEmprunt statutEnCours = statutEmpruntRepository.findByCodeStatut("En cours")
                .orElseGet(() -> {
                    StatutEmprunt newStatut = new StatutEmprunt();
                    newStatut.setCodeStatut("En cours");
                    newStatut.setCodeStatut("Emprunt en cours"); 
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
        if (!"En cours".equalsIgnoreCase(statutActuel) && !"Retard".equalsIgnoreCase(statutActuel)) {
            throw new EmpruntException(
                    "L'emprunt n'est pas dans un état permettant le retour. Statut actuel : " + statutActuel);
        }

        // 3. Vérifier que la date de retour est valide
        if (dateRetourEffective == null) {
            throw new EmpruntException("La date de retour effective ne peut pas être nulle");
        }

        LocalDate dateEmprunt = emprunt.getDateEmprunt();
        if (dateRetourEffective.isBefore(dateEmprunt)) {
            throw new EmpruntException("La date de retour ne peut pas être antérieure à la date d'emprunt");
        }

        // 4. Vérifier que ce n'est pas un jour férié
        if (joursFeriesRepository.existsByDateFerie(dateRetourEffective)) {
            throw new EmpruntException("Le retour ne peut pas être effectué un jour férié");
        }

        // 5. Récupérer l'exemplaire pour remettre à jour la quantité
        Exemplaire exemplaire = exemplaireRepository.findById(emprunt.getExemplaire().getId())
                .orElseThrow(() -> new EmpruntException("L'exemplaire associé à l'emprunt n'existe plus"));

        // 6. Vérifier s'il y a un retard et gérer les pénalités
        LocalDate dateRetourPrevue = emprunt.getDateRetourPrevue();
        boolean enRetard = dateRetourEffective.isAfter(dateRetourPrevue);

        if (enRetard) {
            // Calculer la durée du retard
            long joursRetard = java.time.temporal.ChronoUnit.DAYS.between(dateRetourPrevue, dateRetourEffective);

            // Créer une pénalité
            Penalite penalite = new Penalite();
            penalite.setEmprunt(emprunt);
            penalite.setAdherent(emprunt.getAdherent());
            penalite.setDateDebut(dateRetourEffective);
            penalite.setJour((int) joursRetard); // Durée de la pénalité égale au nombre de jours de retard
            penalite.setRaison("Retard de " + joursRetard + " jour(s) pour l'emprunt ID " + empruntId);

            penaliteRepository.save(penalite);
        }

        // 7. Mettre à jour la quantité d'exemplaires (rendre l'exemplaire disponible)
        exemplaire.setQuantite(exemplaire.getQuantite() + 1);
        exemplaireRepository.save(exemplaire);

        // 8. Enregistrer le mouvement d'emprunt avec statut 'Rendu'
        StatutEmprunt statutRendu = statutEmpruntRepository.findByCodeStatut("Rendu")
        .orElseThrow(() -> new EmpruntException("Le statut 'Rendu' n'existe pas dans la base."));


        MvtEmprunt mvt = new MvtEmprunt();
        mvt.setEmprunt(emprunt);
        mvt.setStatutNouveau(statutRendu);
        mvt.setDateMouvement(dateRetourEffective); // Convertir LocalDate en LocalDateTime
        mvtEmpruntRepository.save(mvt);

        // 9. Supprimer les réservations éventuelles pour ce livre maintenant qu'il est
        // disponible
        List<Reservation> reservations = reservationRepository.findByLivreId(exemplaire.getLivre().getId());
        if (!reservations.isEmpty()) {
            // Vous pouvez choisir de notifier le premier réservateur ou simplement
            // supprimer les réservations
            // Pour cet exemple, on garde les réservations actives
            // reservationRepository.deleteAll(reservations);
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