package com.bibliotheque.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "bibliothecaires")
public class Bibliothecaire {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bibliothecaires_id_gen")
    @SequenceGenerator(name = "bibliothecaires_id_gen", sequenceName = "bibliothecaires_id_bibliothecaire_seq", allocationSize = 1)
    @Column(name = "id_bibliothecaire", nullable = false)
    private Integer id;

    @Column(name = "id_utilisateur", nullable = false, unique = true)
    private Integer idUtilisateur;

    @Column(name = "nom", nullable = false, length = 100)
    private String nom;

    @Column(name = "prenom", nullable = false, length = 100)
    private String prenom;
}