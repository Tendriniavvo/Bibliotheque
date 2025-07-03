package com.bibliotheque.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reservations_id_gen")
    @SequenceGenerator(name = "reservations_id_gen", sequenceName = "reservations_id_reservation_seq", allocationSize = 1)
    @Column(name = "id_reservation", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_livre", nullable = false)
    private Livre livre;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_adherent", nullable = false)
    private Adherent adherent;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_statut", nullable = false)
    private StatutReservation statut;

    @Column(name = "date_demande", nullable = false)
    private Instant dateDemande = Instant.now();

    @Column(name = "date_expiration", nullable = false)
    private LocalDate dateExpiration;
}
