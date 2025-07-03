package com.bibliotheque.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "livres")
public class Livre {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "livre_id_gen")
    @SequenceGenerator(name = "livre_id_gen", sequenceName = "livres_id_livre_seq", allocationSize = 1)
    @Column(name = "id_livre", nullable = false)
    private Integer id;

    @Column(name = "titre", nullable = false, length = 255)
    private String titre;

    @Column(name = "isbn", unique = true, length = 20)
    private String isbn;

    @Column(name = "annee_publication")
    private Integer anneePublication;

    @Column(name = "resume", columnDefinition = "TEXT")
    private String resume;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_editeur")
    private Editeur editeur;
}
