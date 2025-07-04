package com.bibliotheque.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "prolongements")
public class Prolongement {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prolongement_id_gen")
    @SequenceGenerator(name = "prolongement_id_gen", sequenceName = "prolongements_id_prolongement_seq", allocationSize = 1)
    @Column(name = "id_prolongement", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_emprunt", nullable = false)
    private Emprunt emprunt;

    @Column(name = "date_fin", nullable = false)
    private LocalDate dateFin;

    @Column(name = "date_prolongement", nullable = false)
    private LocalDate dateProlongement;
}
