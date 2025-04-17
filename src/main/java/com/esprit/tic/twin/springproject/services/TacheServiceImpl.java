package com.esprit.tic.twin.springproject.services;

import com.esprit.tic.twin.springproject.entities.Etudiant;
import com.esprit.tic.twin.springproject.entities.Tache;

import com.esprit.tic.twin.springproject.repositories.EtudiantRepository;
import com.esprit.tic.twin.springproject.repositories.TacheRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
@Slf4j
@Service
@AllArgsConstructor
public class TacheServiceImpl implements ITacheService {

    private final TacheRepository tacheRepository;
    private final EtudiantRepository etudiantRepository;

    @Override
    public Tache addTache(Tache tache) {
        tache.setTarifFinal(calculerTarif(tache));
        return tacheRepository.save(tache);
    }

    @Override
    public Tache updateTache(Tache tache) {
        if (tacheRepository.existsById(tache.getIdTache())) {
            tache.setTarifFinal(calculerTarif(tache));
            return tacheRepository.save(tache);
        }
        throw new RuntimeException("Tache not found with id: " + tache.getIdTache());
    }

    @Override
    public Tache retrieveTache(Long id) {
        return tacheRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tache not found with id: " + id));
    }

    @Override
    public List<Tache> retrieveAllTaches() {
        return tacheRepository.findAll();
    }

    @Override
    public void removeTache(Long id) {
        if (tacheRepository.existsById(id)) {
            tacheRepository.deleteById(id);
        } else {
            log.info("Tachee not found with id: " + id);
        }
    }

    @Override
    public List<Tache> addTasksAndAffectToEtudiant(List<Tache> tasks, String nomEt, String prenomEt) {
        Etudiant etudiant = etudiantRepository.findByNomEtAndPrenomEt(nomEt, prenomEt)
                .orElseThrow(() -> new RuntimeException("Etudiant not found with nomEt: " + nomEt + " and prenomEt: " + prenomEt));

        for (Tache tache : tasks) {
            tache.setTarifFinal(calculerTarif(tache));
            tache.setEtudiantOrdinaire(etudiant); // Updated to setEtudiantOrdinaire
        }
        return tacheRepository.saveAll(tasks);
    }

    public double calculerTarif(Tache tache) {
        Set<LocalDate> joursFeries = Set.of(
                LocalDate.of(LocalDate.now().getYear(), 1, 1),
                LocalDate.of(LocalDate.now().getYear(), 3, 20),
                LocalDate.of(LocalDate.now().getYear(), 7, 25),
                LocalDate.of(LocalDate.now().getYear(), 4, 9)
        );

        double tarifFinal = tache.getTarifHoraire() * tache.getDuree();

        tarifFinal *= switch (tache.getTypeTache()) {
            case MENAGERE -> 1.05;
            case JARDINAGE -> 1.10;
            case BRICOLAGE -> 1.15;
        };

        if (joursFeries.contains(tache.getDateTache())) {
            tarifFinal *= 1.2;
        }

        if (tache.getDuree() > 5) {
            tarifFinal *= 0.9;
        }

        return Math.min(Math.max(tarifFinal, 10.0), 500.0);
    }
}