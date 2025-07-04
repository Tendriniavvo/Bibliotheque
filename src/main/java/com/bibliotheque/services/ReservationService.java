package com.bibliotheque.services;

import com.bibliotheque.entities.*;
import com.bibliotheque.exceptions.ReservationException;
import com.bibliotheque.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final PenaliteRepository penaliteRepository;
    private final ExemplaireRepository exemplaireRepository;
    private final EmpruntService empruntService;
    private final StatutReservationRepository statutReservationRepository;
    private final MvtReservationRepository mvtReservationRepository;
    private final JourFerieRepository joursFeriesRepository;
    private final AbonnementRepository abonnementRepository;
    private final AdherentRepository adherentRepository;
    private final ProfilsAdherentRepository profilAdherentRepository;

    @Autowired
    public ReservationService(
            ReservationRepository reservationRepository,
            PenaliteRepository penaliteRepository,
            ExemplaireRepository exemplaireRepository,
            EmpruntService empruntService,
            StatutReservationRepository statutReservationRepository,
            MvtReservationRepository mvtReservationRepository,
            JourFerieRepository joursFeriesRepository,
            AbonnementRepository abonnementRepository,
            AdherentRepository adherentRepository,
            ProfilsAdherentRepository profilsAdherentRepository,
            ProfilsAdherentRepository profilAdherentRepository) {
        this.reservationRepository = reservationRepository;
        this.penaliteRepository = penaliteRepository;
        this.exemplaireRepository = exemplaireRepository;
        this.empruntService = empruntService;
        this.statutReservationRepository = statutReservationRepository;
        this.mvtReservationRepository = mvtReservationRepository;
        this.joursFeriesRepository = joursFeriesRepository;
        this.abonnementRepository = abonnementRepository;
        this.adherentRepository = adherentRepository;
        this.profilAdherentRepository = profilAdherentRepository;
    }

    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }

    @Transactional
    public Reservation save(Reservation reservation) throws ReservationException {
        // 1. Validation des données d'entrée
        if (reservation == null || reservation.getAdherent() == null || reservation.getLivre() == null) {
            throw new ReservationException("La réservation, l'adhérent ou le livre ne peut pas être nul.");
        }

        // 2. Recharge l'adhérent pour avoir le profil complet
        Adherent adherent = adherentRepository.findById(reservation.getAdherent().getId())
                .orElseThrow(() -> new ReservationException("Adhérent introuvable."));

        // 3. Vérification de l'abonnement actif
        LocalDate dateAReserver = reservation.getDateAReserver().toLocalDate();
        boolean hasActiveAbonnement = abonnementRepository.existsByAdherentIdAndDateDebutLessThanEqualAndDateFinGreaterThanEqual(
                adherent.getId(), dateAReserver, dateAReserver);
        if (!hasActiveAbonnement) {
            throw new ReservationException(
                    "L'adhérent n'a pas d'abonnement actif à la date de réservation (" + dateAReserver + ").");
        }

        // 4. Vérification des pénalités en cours
        List<Penalite> penalites = penaliteRepository.findActivePenalitesByAdherent(adherent.getId(), dateAReserver);
        for (Penalite penalite : penalites) {
            LocalDate debut = penalite.getDateDebut();
            LocalDate fin = debut.plusDays(penalite.getJour() - 1);
            if (!dateAReserver.isBefore(debut) && !dateAReserver.isAfter(fin)) {
                throw new ReservationException(
                        "L'adhérent a une pénalité active du " + debut + " au " + fin + " et ne peut pas réserver.");
            }
        }

        // 5. Vérification du quota de réservations simultanées
        ProfilsAdherent profil = profilAdherentRepository.findById(adherent.getIdProfil())
                .orElseThrow(() -> new ReservationException("Le profil de l'adhérent n'existe pas."));
        long reservationsActives = reservationRepository.countActiveReservationsByAdherent(adherent.getId(), dateAReserver);
        if (reservationsActives >= profil.getQuotaEmpruntsSimultanes()) {
            throw new ReservationException("L'adhérent a déjà atteint son quota de réservations simultanées.");
        }

        // 6. Vérification des jours fériés
        if (joursFeriesRepository.existsByDateFerie(dateAReserver)) {
            throw new ReservationException("La date de réservation (" + dateAReserver + ") est un jour férié.");
        }

        // 7. Vérification de la validité des dates
        if (reservation.getDateDemande() == null) {
            reservation.setDateDemande(LocalDateTime.now());
        }
        if (reservation.getDateAReserver() == null || dateAReserver.isBefore(LocalDate.now())) {
            throw new ReservationException("La date de réservation doit être aujourd'hui ou dans le futur.");
        }

        // 8. Vérification de la disponibilité des exemplaires
        Livre livre = reservation.getLivre();
        List<Exemplaire> exemplaires = exemplaireRepository.findByLivreId(livre.getId());
        if (exemplaires.isEmpty()) {
            throw new ReservationException("Aucun exemplaire disponible pour ce livre.");
        }

        int totalExemplaires = exemplaires.stream().mapToInt(Exemplaire::getQuantite).sum();
        int reservationsActivesOnDate = reservationRepository.countActiveReservationsByLivreAndDate(livre.getId(), dateAReserver);
        long empruntsActifs = empruntService.countActiveEmpruntsByLivreAndDate(livre.getId(), dateAReserver);

        if (totalExemplaires <= (reservationsActivesOnDate + empruntsActifs)) {
            throw new ReservationException(
                    "Aucun exemplaire disponible pour ce livre à la date demandée (" + dateAReserver + ").");
        }

        // 9. Sauvegarde de la réservation
        reservation.setAdherent(adherent); // Assurer que l'adhérent complet est associé
        Reservation savedReservation = reservationRepository.save(reservation);

        // 10. Création du mouvement de réservation avec statut "En attente"
        StatutReservation statutEnAttente = statutReservationRepository.findByCodeStatut("En attente")
                .orElseThrow(() -> new ReservationException("Statut 'En attente' introuvable."));
        MvtReservation mvt = new MvtReservation();
        mvt.setReservation(savedReservation);
        mvt.setStatutNouveau(statutEnAttente);
        mvt.setDateMouvement(LocalDateTime.now());
        mvtReservationRepository.save(mvt);

        return savedReservation;
    }

    public Optional<Reservation> findById(Integer id) {
        return reservationRepository.findById(id);
    }

    public void deleteById(Integer id) {
        reservationRepository.deleteById(id);
    }
}