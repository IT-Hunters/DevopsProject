package com.esprit.tic.twin.springproject.controllers;

import com.esprit.tic.twin.springproject.entities.Foyer;
import com.esprit.tic.twin.springproject.entities.Universite;
import com.esprit.tic.twin.springproject.services.IUniversiteService;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/universite")
public class UniversiteRestController {
    IUniversiteService universiteService;

    @PostMapping("/add-universite")
    public Universite addUniversite(@RequestBody Universite u) {
        return universiteService.addUniversite(u);
    }

    @GetMapping("/retrieve-all-universites")
    public List<Universite> getUniversites() {
        return universiteService.retrieveAllUniversites();
    }

    @PutMapping("/update-universite")
    public Universite updateUniversite(@RequestBody Universite e) {
        return universiteService.updateUniversite(e);
    }

    @DeleteMapping("/remove-universite/{universite-id}")
    public void removeUniversite(@PathVariable("universite-id") Long idUniversrite) {
        universiteService.removeUniversite(idUniversrite);
    }
    @PutMapping("/affecter-foyer-unviversite/{idFoyer}/{nom}")
    public Universite affectUniversite(@PathVariable("idFoyer") Long idFoyer, @PathVariable("nom") String nom){
        System.out.println(nom);
        return universiteService.affecterFoyerAUniversite(idFoyer,nom);
    }
    @PutMapping("/desaffecter-foyer-unviversite/{idFoyer}")
    public Universite affectUniversite(@PathVariable("idFoyer") Long idFoyer){
        return universiteService.desaffecterFoyerAUniversite(idFoyer);
    }
}
