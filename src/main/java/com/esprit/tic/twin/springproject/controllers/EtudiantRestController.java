package com.esprit.tic.twin.springproject.controllers;

import com.esprit.tic.twin.springproject.entities.Etudiant;
import com.esprit.tic.twin.springproject.services.IEtudiantService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/etudiant")
public class EtudiantRestController {
    IEtudiantService etudiantService;
    @PutMapping("/affecter-etudiant-reservation/{nomEt}/{prenomEt}/{idReservation}")
    public Etudiant affecterEtudiantAReservation(@PathVariable("nomEt") String nomEt,
                                                 @PathVariable("prenomEt") String prenomEt,
                                                 @PathVariable("idReservation") String idReservation) {
        return etudiantService.affecterEtudiantAReservation(nomEt,prenomEt,idReservation);
    }
    @GetMapping("/calculNouveauMontantInscriptionDesEtudiants")
    public HashMap<String,Float> calculNouveauMontantInscriptionDesEtudiants(){
        return etudiantService.calculNouveauMontantInscriptionDesEtudiants();
    }
    @GetMapping("/calculTopPerformingStudentsByTaches/{topN}")
    public List<Etudiant> calculTopPerformingStudentsByTaches(@PathVariable("topN") int topN){
        return etudiantService.calculTopPerformingStudentsByTaches(topN);
    }

}
