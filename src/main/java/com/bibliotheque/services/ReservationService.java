package com.bibliotheque.services;

import com.bibliotheque.entities.*;
import com.bibliotheque.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
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

    @Autowired
    public ReservationService(
            ReservationRepository reservationRepository,
            PenaliteRepository penaliteRepository,
            ExemplaireRepository exemplaireRepository,
            EmpruntService empruntService,
            StatutReservationRepository statutReservationRepository,
            MvtReservationRepository mvtReservationRepository) {
        this.reservationRepository = reservationRepository;
        this.penaliteRepository = penaliteRepository;
        this.exemplaireRepository = exemplaireRepository;
        this.empruntService = empruntService;
        this.statutReservationRepository = statutReservationRepository;
        this.mvtReservationRepository = mvtReservationRepository;
    }

    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }

    public Reservation save(Reservation reservation) {
        // Vérification des pénalités pour la date de réservation
        Integer adherentId = reservation.getAdherent().getId();
        List<Penalite> penalites = penaliteRepository.findByAdherentId(adherentId);
        Instant dateResa = reservation.getDateDemande();
        for (Penalite p : penalites) {
            LocalDate debutLocal = p.getDateDebut();
            LocalDate finLocal = debutLocal.plusDays(p.getJour() - 1);
            Instant debut = debutLocal.atStartOfDay(ZoneId.systemDefault()).toInstant();
            Instant fin = finLocal.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();
            if ((dateResa.equals(debut) || dateResa.isAfter(debut)) && dateResa.isBefore(fin)) {
                throw new IllegalStateException("L'adhérent a une pénalité couvrant la date de réservation et ne peut pas réserver.");
            }
        }

        // Vérification de la disponibilité d'exemplaires pour la date de réservation
        Integer livreId = reservation.getLivre().getId();
        List<Exemplaire> exemplaires = exemplaireRepository.findByLivreId(livreId);
        int total = exemplaires.stream().mapToInt(Exemplaire::getQuantite).sum();
        int disponible = total;
        for (Exemplaire ex : exemplaires) {
            List<Emprunt> empruntsEx = empruntService.findAll().stream()
                    .filter(e -> e.getExemplaire().getId().equals(ex.getId()))
                    .toList();
            for (Emprunt e : empruntsEx) {
                String statut = empruntService.getLastStatutForEmprunt(e.getId());
                Instant debutExist = e.getDateEmprunt();
                Instant finExist = e.getDateRetourPrevue();
                Instant dateResaDebut = dateResa.atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant();
                Instant dateResaFin = dateResa.atZone(ZoneId.systemDefault()).toLocalDate().atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant();
                boolean chevauche = !dateResaFin.isBefore(debutExist.atZone(ZoneId.systemDefault()).toInstant()) &&
                                    !dateResaDebut.isAfter(finExist.atZone(ZoneId.systemDefault()).toInstant());
                if (("En cours".equalsIgnoreCase(statut) || "Retard".equalsIgnoreCase(statut)) && chevauche) {
                    disponible -= 1;
                } else if ("Rendu".equalsIgnoreCase(statut) && chevauche) {
                    disponible += 1;
                }
            }
        }
        if (disponible <= 0) {
            throw new IllegalStateException("Aucun exemplaire disponible pour ce livre à la date demandée.");
        }

        // Sauvegarde de la réservation
        Reservation savedReservation = reservationRepository.save(reservation);

        // Création du mouvement de réservation avec statut "En attente"
        StatutReservation statutEnAttente = statutReservationRepository.findByCodeStatut("En attente")
                .orElseThrow(() -> new IllegalStateException("Statut 'En attente' introuvable"));

        MvtReservation mvt = new MvtReservation();
        mvt.setReservation(savedReservation);
        mvt.setStatutNouveau(statutEnAttente);
        mvt.setDateMouvement(Instant.now());
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