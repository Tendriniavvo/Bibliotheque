package com.bibliotheque.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "profils_adherent")
public class ProfilsAdherent {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "profils_adherent_id_gen")
    @SequenceGenerator(name = "profils_adherent_id_gen", sequenceName = "profils_adherent_id_profil_seq", allocationSize = 1)
    @Column(name = "id_profil", nullable = false)
    private Integer id;

    @Column(name = "nom_profil", nullable = false, length = 100)
    private String nomProfil;

    @ColumnDefault("3")
    @Column(name = "quota_emprunts_simultanes", nullable = false)
    private Integer quotaEmpruntsSimultanes;

    @ColumnDefault("14")
    @Column(name = "quota_jours_pret", nullable = false)
    private Integer quotaJoursPret;

    @ColumnDefault("2")
    @Column(name = "quota_reservation_livre", nullable = false)
    private Integer quotaReservationLivre;

    @ColumnDefault("1")
    @Column(name = "quota_prolongement_pret", nullable = false)
    private Integer quotaProlongementPret;

    @ColumnDefault("7")
    @Column(name = "jours_penalite", nullable = false)
    private Integer joursPenalite;
}