package com.esprit.tic.twin.springproject.controllers;

import com.esprit.tic.twin.springproject.entities.Tache;
import com.esprit.tic.twin.springproject.services.ITacheService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/taches")
@CrossOrigin(origins = "http://localhost:4200")
public class TacheRestController {
    ITacheService tacheService;

    @PostMapping
    public Tache addTache(@RequestBody Tache tache) {
        return tacheService.addTache(tache);
    }

    @PutMapping
    public Tache updateTache(@RequestBody Tache tache) {
        return tacheService.updateTache(tache);
    }

    @GetMapping("/{id}")
    public Tache retrieveTache(@PathVariable Long id) {
        return tacheService.retrieveTache(id);
    }

    @GetMapping
    public List<Tache> retrieveAllTaches() {
        return tacheService.retrieveAllTaches();
    }

    @DeleteMapping("/{id}")
    public void removeTache(@PathVariable Long id) {
        tacheService.removeTache(id);
    }

    @PostMapping("/add-tasks-affect-etudiant/{nomEt}/{prenomEt}")
    public List<Tache> addTasksAndAffectToEtudiant(@RequestBody List<Tache> tasks,
                                                   @PathVariable("nomEt") String nomEt,
                                                   @PathVariable("prenomEt") String prenomEt) {
        return tacheService.addTasksAndAffectToEtudiant(tasks, nomEt, prenomEt);
    }
}