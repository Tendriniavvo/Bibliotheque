package com.bibliotheque.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "penalites")
public class Penalite {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "penalites_id_gen")
    @SequenceGenerator(name = "penalites_id_gen", sequenceName = "penalites_id_penalite_seq", allocationSize = 1)
    @Column(name = "id_penalite", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_emprunt", nullable = false)
    private Emprunt emprunt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_adherent", nullable = false)
    private Adherent adherent;

    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;

    @Column(name = "jour", nullable = false)
    private Integer jour;

    @Column(name = "raison")
    private String raison;
}
