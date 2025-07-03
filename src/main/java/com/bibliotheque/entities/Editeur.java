package com.bibliotheque.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "editeurs")
public class Editeur {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "editeurs_id_gen")
    @SequenceGenerator(name = "editeurs_id_gen", sequenceName = "editeurs_id_editeur_seq", allocationSize = 1)
    @Column(name = "id_editeur", nullable = false)
    private Integer id;

    @Column(name = "nom", nullable = false, length = 150)
    private String nom;

}