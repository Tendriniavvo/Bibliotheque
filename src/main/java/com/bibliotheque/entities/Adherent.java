package com.bibliotheque.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "adherents")
public class Adherent {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "adherents_id_gen")
    @SequenceGenerator(name = "adherents_id_gen", sequenceName = "adherents_id_adherent_seq", allocationSize = 1)
    @Column(name = "id_adherent", nullable = false)
    private Integer id;

    @Column(name = "id_utilisateur", nullable = false, unique = true)
    private Integer idUtilisateur;

    @Column(name = "id_profil", nullable = false)
    private Integer idProfil;

    @Column(name = "nom", nullable = false, length = 100)
    private String nom;

    @Column(name = "prenom", nullable = false, length = 100)
    private String prenom;

    @Column(name = "date_naissance", nullable = false)
    private LocalDate dateNaissance;

    @ColumnDefault("CURRENT_DATE")
    @Column(name = "date_inscription", nullable = false)
    private LocalDate dateInscription;
}
