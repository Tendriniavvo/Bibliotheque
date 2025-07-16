package com.bibliotheque.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "statuts_reservation")
public class StatutReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_statut", nullable = false)
    private Integer id;

    @Column(name = "code_statut", nullable = false, unique = true, length = 20)
    private String codeStatut;
}
