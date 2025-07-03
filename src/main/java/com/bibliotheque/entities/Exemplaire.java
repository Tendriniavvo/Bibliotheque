package com.bibliotheque.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "exemplaires")
public class Exemplaire {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exemplaires_id_gen")
    @SequenceGenerator(name = "exemplaires_id_gen", sequenceName = "exemplaires_id_exemplaire_seq", allocationSize = 1)
    @Column(name = "id_exemplaire", nullable = false)
    private Integer id;

    @Column(name = "quantite", nullable = false)
    private Integer quantite;

}