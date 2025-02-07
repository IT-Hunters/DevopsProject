package com.esprit.tic.twin.springproject.services;

import com.esprit.tic.twin.springproject.entities.Foyer;
import com.esprit.tic.twin.springproject.entities.Universite;

import java.util.List;
import java.util.Optional;

public interface IUniversiteService {

    Universite affecterFoyerAUniversite(Long idFoyer, String nomUniversite);
    Universite desaffecterFoyerAUniversite(Long idFoyer);
    List<Universite> retrieveAllUniversites();
    Universite addUniversite(Universite u);
    Universite updateUniversite(Universite u);
    Universite retrieveUniversite(Long idUniversite);
    void removeUniversite(Long idUniversite);

}
