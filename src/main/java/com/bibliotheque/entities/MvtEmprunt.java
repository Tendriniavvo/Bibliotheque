package com.bibliotheque.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

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
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_emprunt", nullable = false)
    private Emprunt idEmprunt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "date_mouvement", nullable = false)
    private Instant dateMouvement;

}