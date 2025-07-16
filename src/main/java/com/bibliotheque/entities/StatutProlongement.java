package com.bibliotheque.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "statuts_prolongement")
public class StatutProlongement {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "statuts_prolongement_id_gen")
    @SequenceGenerator(name = "statuts_prolongement_id_gen", sequenceName = "statuts_prolongement_id_statut_seq", allocationSize = 1)
    @Column(name = "id_statut", nullable = false)
    private Integer id;

    @Column(name = "code_statut", nullable = false, length = 20)
    private String codeStatut;
}