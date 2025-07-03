package com.bibliotheque.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "type_emprunts")
public class TypeEmprunt {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "type_emprunts_id_gen")
    @SequenceGenerator(name = "type_emprunts_id_gen", sequenceName = "type_emprunts_id_type_emprunt_seq", allocationSize = 1)
    @Column(name = "id_type_emprunt", nullable = false)
    private Integer id;

    @Column(name = "nom_type", length = 50)
    private String nomType;

}