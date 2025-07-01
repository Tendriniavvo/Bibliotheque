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

    @ColumnDefault("21")
    @Column(name = "duree_pret_domicile_jours", nullable = false)
    private Integer dureePretDomicileJours;

    @ColumnDefault("3")
    @Column(name = "duree_pret_sur_place_heures", nullable = false)
    private Integer dureePretSurPlaceHeures;

    @ColumnDefault("true")
    @Column(name = "peut_prolonger_pret", nullable = false)
    private Boolean peutProlongerPret = false;

    @ColumnDefault("1")
    @Column(name = "jours_penalite_par_retard", nullable = false)
    private Integer joursPenaliteParRetard;

}