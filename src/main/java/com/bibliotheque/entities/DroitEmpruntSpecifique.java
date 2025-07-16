package com.bibliotheque.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
    name = "droits_emprunt_specifiques",
    uniqueConstraints = @UniqueConstraint(columnNames = {"id_livre", "id_profil"})
)
public class DroitEmpruntSpecifique {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "droits_id_gen")
    @SequenceGenerator(name = "droits_id_gen", sequenceName = "droits_emprunt_specifiques_id_droit_seq", allocationSize = 1)
    @Column(name = "id_droit", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_livre", nullable = false)
    private Livre livre;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_profil", nullable = false)
    private ProfilsAdherent profil;

    @Column(name = "age")
    private Integer age;

    @Column(name = "emprunt_surplace_autorise", nullable = false)
    private Boolean empruntSurplaceAutorise = true;

    @Column(name = "emprunt_domicile_autorise", nullable = false)
    private Boolean empruntDomicileAutorise = true;
}