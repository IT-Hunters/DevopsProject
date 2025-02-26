package com.esprit.tic.twin.springproject.controllers;

import com.esprit.tic.twin.springproject.entities.Reservation;
import com.esprit.tic.twin.springproject.entities.Tache;
import com.esprit.tic.twin.springproject.services.IReservationService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@AllArgsConstructor
public class ReservationRestController {
    IReservationService reservationService;
    @GetMapping("/getReservationParAnneeUniversitaire/{dateDebut}/{dateFin}")
    public List<Reservation> getReservationParAnneeUniversitaire(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateDebut,
                                                                 @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateFin) {
        return reservationService.getReservationParAnneeUniversitaire(dateDebut,dateFin);
    }

@PostMapping("ajouterReservationEtAssignerAChambreEtAEtudiant/{numChambre}/{cin}")
    public Reservation ajouterReservationEtAssignerAChambreEtAEtudiant(@RequestBody Reservation res,@PathVariable Long numChambre, @PathVariable long cin){
        return reservationService.ajouterReservationEtAssignerAChambreEtAEtudiant(res,numChambre,cin);
    }


}

