package com.bibliotheque.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "mvt_emprunt")
public class MvtEmprunt {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mvt_emprunt_id_gen")
    @SequenceGenerator(name = "mvt_emprunt_id_gen", sequenceName = "mvt_emprunt_id_mvt_emprunt_seq", allocationSize = 1)
    @Column(name = "id_mvt_emprunt", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_emprunt", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Emprunt emprunt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_statut_nouveau", nullable = false)
    private StatutEmprunt statutNouveau;

    @Column(name = "date_mouvement", nullable = false, updatable = false)
    @ColumnDefault("CURRENT_TIMESTAMP")
    private LocalDate dateMouvement;
}
