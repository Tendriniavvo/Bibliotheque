package com.film.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "employe")
public class Employe {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employe_id_gen")
    @SequenceGenerator(name = "employe_id_gen", sequenceName = "employe_id_emp_seq", allocationSize = 1)
    @Column(name = "id_emp", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_dept")
    private Department department;

    @Column(name = "nom_emp")
    private String nomEmp;

    @Column(name = "num_emp")
    private String numEmp;

    @Column(name = "birth")
    private LocalDate birth;

}