package com.bibliotheque.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "auteurs")
public class Auteur {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "auteurs_id_gen")
    @SequenceGenerator(name = "auteurs_id_gen", sequenceName = "auteurs_id_auteur_seq", allocationSize = 1)
    @Column(name = "id_auteur", nullable = false)
    private Integer id;

    @Column(name = "nom", nullable = false, length = 100)
    private String nom;

    @Column(name = "prenom", length = 100)
    private String prenom;

}