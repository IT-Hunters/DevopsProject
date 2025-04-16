package com.esprit.tic.twin.springproject.controllers;


import com.esprit.tic.twin.springproject.entities.Tache;
import com.esprit.tic.twin.springproject.services.ITacheService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/taches")
@CrossOrigin(origins = {"http://localhost:4201", "http://localhost:4200","http://localhost:80" })
public class TacheRestController {
    ITacheService tacheService;

    @PostMapping("/add-tasks-affect-etudiant/{nomEt}/{prenomEt}")
    public List<Tache> addTasksAndAffectToEtudiant(@RequestBody List<Tache> tasks,
                                                   @PathVariable("nomEt") String nomEt,
                                                   @PathVariable("prenomEt") String prenomEt) {
        return tacheService.addTasksAndAffectToEtudiant(tasks, nomEt, prenomEt);
    }

}
