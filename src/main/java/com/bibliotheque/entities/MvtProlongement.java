package com.bibliotheque.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "mvt_prolongement")
public class MvtProlongement {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mvt_prolongement_id_gen")
    @SequenceGenerator(name = "mvt_prolongement_id_gen", sequenceName = "mvt_prolongement_id_mvt_prolongement_seq", allocationSize = 1)
    @Column(name = "id_mvt_prolongement", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_prolongement", nullable = false)
    private Prolongement prolongement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_statut_nouveau", nullable = false)
    private StatutProlongement statutNouveau;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "date_mouvement", nullable = false)
    private LocalDate dateMouvement;
}