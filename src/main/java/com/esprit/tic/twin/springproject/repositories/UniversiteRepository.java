package com.esprit.tic.twin.springproject.repositories;

import com.esprit.tic.twin.springproject.entities.Universite;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface UniversiteRepository extends JpaRepository<Universite, Long> {
    Universite findByNomUniversite(String nom);
}
