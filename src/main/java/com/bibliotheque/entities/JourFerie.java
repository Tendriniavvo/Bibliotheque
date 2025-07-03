package com.bibliotheque.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "jours_feries")
public class JourFerie {

    @Id
    @Column(name = "date_ferie", nullable = false)
    private LocalDate dateFerie;

    @Column(name = "description", length = 255)
    private String description;
}
