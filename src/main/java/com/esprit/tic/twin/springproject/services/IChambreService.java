package com.esprit.tic.twin.springproject.services;

import com.esprit.tic.twin.springproject.entities.Chambre;
import com.esprit.tic.twin.springproject.entities.TypeChambre;

import java.util.List;
import java.util.Map;

public interface IChambreService {
    long nbChambreParTypeEtBloc(TypeChambre type, long idBloc);
    Map<String, Float> pourcentageChambreParTypeChambre();
    void nbPlacesDisponiblesParChambreAnneeEnCours();
    List<Chambre> retrieveAllChambres();
    List<Chambre> getChambresParNomBloc(String nomBloc);
    Chambre addChambre(Chambre c);
    Chambre updateChambre(Chambre c);
    Chambre retrieveChambre(Long idChambre);
    void removeChambre(Long idChambre);
    List<Chambre> findAvailableChambresForCapacity(int minCapacity, TypeChambre type);
}