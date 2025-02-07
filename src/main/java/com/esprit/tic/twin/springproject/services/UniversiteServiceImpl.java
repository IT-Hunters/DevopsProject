package com.esprit.tic.twin.springproject.services;

import com.esprit.tic.twin.springproject.entities.Foyer;
import com.esprit.tic.twin.springproject.entities.Universite;
import com.esprit.tic.twin.springproject.repositories.FoyerRepository;
import com.esprit.tic.twin.springproject.repositories.UniversiteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UniversiteServiceImpl implements IUniversiteService{
    UniversiteRepository universiteRepository;
    FoyerRepository foyerRepository;

    @Override
    public Universite affecterFoyerAUniversite(Long idFoyer, String nomUniversite) {
        Foyer fr = foyerRepository.getReferenceById(idFoyer);
        Universite un = universiteRepository.findByNomUniversite(nomUniversite);
            fr.setUniversite(un);
            foyerRepository.save(fr);
            return un;
    }
    @Override
    public Universite desaffecterFoyerAUniversite(Long idFoyer) {
        Foyer fr = foyerRepository.getReferenceById(idFoyer);
        fr.setUniversite(null);
        foyerRepository.save(fr);
        return null;
    }

    @Override
    public List<Universite> retrieveAllUniversites() {
        return universiteRepository.findAll();
    }

    @Override
    public Universite addUniversite(Universite u) {
        return universiteRepository.save(u);
    }

    @Override
    public Universite updateUniversite(Universite u) {
        return universiteRepository.save(u);
    }

    @Override
    public Universite retrieveUniversite(Long idUniversite) {
        return universiteRepository.findById(idUniversite).orElse(null);
    }

    @Override
    public void removeUniversite(Long idUniversite) {
        universiteRepository.deleteById(idUniversite);
    }

}
