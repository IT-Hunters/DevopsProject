package com.esprit.tic.twin.springproject.controllers;

import com.esprit.tic.twin.springproject.entities.Tache;
import com.esprit.tic.twin.springproject.services.ITacheService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/taches")
@CrossOrigin(origins = "http://localhost:4202")
public class TacheRestController {

}