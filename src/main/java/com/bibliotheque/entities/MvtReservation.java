package com.bibliotheque.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "mvt_reservation")
public class MvtReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mvt_reservation_id_gen")
    @SequenceGenerator(name = "mvt_reservation_id_gen", sequenceName = "mvt_reservation_id_mvt_reservation_seq", allocationSize = 1)
    @Column(name = "id_mvt_reservation", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_reservation", nullable = false)
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_statut_nouveau", nullable = false)
    private StatutReservation statutNouveau;

    @Column(name = "date_mouvement", nullable = false)
    private LocalDateTime dateMouvement;
}
