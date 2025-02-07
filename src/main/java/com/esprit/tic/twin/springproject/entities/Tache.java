package com.esprit.tic.twin.springproject.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode
@ToString
public class Tache implements Serializable {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long idTache;
    private LocalDate dateTache;
    private int duree;
    private float tarifHoraire;
    @Enumerated(EnumType.STRING)
    private TypeTache typeTache;
    @ManyToOne
    Etudiant etudiantOrdinaire;
    @ManyToOne()
    @JoinColumn(name = "etudiant_responsable_id")
    private Etudiant etudiantResponsable;
}
