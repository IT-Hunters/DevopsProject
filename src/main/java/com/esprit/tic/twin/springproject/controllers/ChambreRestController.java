package com.esprit.tic.twin.springproject.controllers;

import com.esprit.tic.twin.springproject.entities.Chambre;
import com.esprit.tic.twin.springproject.entities.TypeChambre;
import com.esprit.tic.twin.springproject.services.IChambreService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chambre")
@AllArgsConstructor
public class ChambreRestController {
    IChambreService chambreService;
    @GetMapping("/retrieve-chambe-byBlocName")
    public List<Chambre> retrieveChambreByBlocName(@PathVariable String nomBloc){
        return chambreService.getChambresParNomBloc(nomBloc);
    }
    @GetMapping("/nbChambreParTypeEtBloc/{type}/{idBloc}")
    public long nbChambreParTypeEtBloc(@PathVariable TypeChambre type, @PathVariable long idBloc){
        return chambreService.nbChambreParTypeEtBloc(type,idBloc);
    }
}
