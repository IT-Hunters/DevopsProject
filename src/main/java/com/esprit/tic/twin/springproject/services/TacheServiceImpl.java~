package com.esprit.tic.twin.springproject.services;

import com.esprit.tic.twin.springproject.entities.Etudiant;
import com.esprit.tic.twin.springproject.entities.Tache;
import com.esprit.tic.twin.springproject.repositories.EtudiantRepository;
import com.esprit.tic.twin.springproject.repositories.TacheRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TacheServiceImpl implements ITacheService{
    TacheRepository tacheRepository;
    EtudiantRepository etudiantRepository;
    public List<Tache> addTasksAndAffectToEtudiant (List<Tache> tasks, String nomEt, String prenomEt ){
        tacheRepository.saveAll(tasks);
        Etudiant et =  etudiantRepository.findEtudiantByNomEtAndPrenomEt(nomEt,prenomEt).get();
        List<Tache> Oldtaches = new ArrayList<>(et.getTaches().stream().toList());
        Oldtaches.addAll(tasks);
        et.setTaches(Oldtaches.stream().collect(Collectors.toSet()));
        etudiantRepository.save(et);
        return et.getTaches().stream().toList();
    }
    @Override
    public List<Tache> retrieveAllTaches() {
        return tacheRepository.findAll();
    }

    @Override
    public Tache addTache(Tache t) {
        return tacheRepository.save(t);
    }

    @Override
    public Tache updateTache(Tache t) {
        return tacheRepository.save(t);
    }

    @Override
    public Tache retrieveTache(Long idTache) {
        return tacheRepository.findById(idTache).orElse(null);
    }

    @Override
    public void removeTache(Long idTache) {
        tacheRepository.deleteById(idTache);
    }
}
