package com.esprit.tic.twin.springproject.services;

import com.esprit.tic.twin.springproject.entities.Chambre;
import java.util.List;
import com.esprit.tic.twin.springproject.entities.TypeChambre;

public interface IChambreService {

    List<Chambre> retrieveAllChambres();
    Chambre addChambre(Chambre c);
    Chambre updateChambre(Chambre c);
    Chambre retrieveChambre(Long idChambre);
    void removeChambre(Long idChambre);
    List<Chambre> getChambresParNomBloc( String nomBloc);
    long nbChambreParTypeEtBloc( TypeChambre type, long idBloc) ;
}
