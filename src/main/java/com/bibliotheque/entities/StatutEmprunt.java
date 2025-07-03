package com.bibliotheque.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "statuts_emprunt")
public class StatutEmprunt {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "statut_id_gen")
    @SequenceGenerator(name = "statut_id_gen", sequenceName = "statuts_emprunt_id_statut_seq", allocationSize = 1)
    @Column(name = "id_statut", nullable = false)
    private Integer id;

    @Column(name = "code_statut", nullable = false, unique = true, length = 20)
    private String codeStatut;
}
