package com.esprit.tic.twin.springproject.services;

import com.esprit.tic.twin.springproject.entities.Bloc;
import com.esprit.tic.twin.springproject.entities.Chambre;
import com.esprit.tic.twin.springproject.entities.Foyer;
import com.esprit.tic.twin.springproject.entities.TypeChambre;
import com.esprit.tic.twin.springproject.repositories.BlocRepository;
import com.esprit.tic.twin.springproject.repositories.ChambreRepository;
import com.esprit.tic.twin.springproject.repositories.FoyerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;

import static org.hibernate.internal.util.collections.ArrayHelper.forEach;

@Service
@AllArgsConstructor
@Slf4j
public class ChambreServiceImpl implements IChambreService{
    ChambreRepository chambreRepository;
    @Override
    public long nbChambreParTypeEtBloc(TypeChambre type, long idBloc){
        return chambreRepository.findChambreByBloc_IdBlocAndAndTypeC(idBloc,type).toArray().length;
    }
   // @Scheduled(cron= "*/2 * * * * *")
    public void pourcentageChambreParTypeChambre(){
        List<Chambre> listB = chambreRepository.findAll();
        float total = listB.stream().toArray().length;
        for (int i=0;i<3;i++){
            if (i==1) log.info("Simple " + chambreRepository.getNbrTypeC(TypeChambre.SIMPLE)*100/total);
            if (i==1) log.info("Double " + chambreRepository.getNbrTypeC(TypeChambre.DOUBLE)*100/total);
            if (i==1) log.info("Triple " + chambreRepository.getNbrTypeC(TypeChambre.TRIPLE)*100/total);
        }
    }
   // @Scheduled(cron= "*/2 * * * * *")
    public void nbPlacesDisponiblesParChambreAnneeEnCours(){
        List<Chambre> listB = chambreRepository.findChambreByReservations_AnneeUniversitaire_Year(Year.now().getValue());
        listB.forEach( c -> {
            int places = 0;
            if (c.getTypeC() == TypeChambre.SIMPLE ) {  places = 1 - c.getReservations().toArray().length;}
            if (c.getTypeC() == TypeChambre.DOUBLE ) {  places = 2 - c.getReservations().toArray().length;}
            if (c.getTypeC() == TypeChambre.TRIPLE ) {  places = 3 - c.getReservations().toArray().length;}
            log.info("Nb de place restants pour l'année " + Year.now()
                    + "Chambre : " + c.getNumeroChambre()
                    + "est egale a: " + places);
        });

    }
    @Override
    public List<Chambre> retrieveAllChambres() {
        return chambreRepository.findAll();
    }
    @Override
    public List<Chambre> getChambresParNomBloc( String nomBloc){
        return chambreRepository.findChambreByBloc_NomBloc(nomBloc);
    }


    @Override
    public Chambre addChambre(Chambre c) {
        return chambreRepository.save(c);
    }

    @Override
    public Chambre updateChambre(Chambre c) {
        return chambreRepository.save(c);
    }

    @Override
    public Chambre retrieveChambre(Long idChambre) {
        return chambreRepository.findById(idChambre).orElse(null);
    }

    @Override
    public void removeChambre(Long idChambre) {
        chambreRepository.deleteById(idChambre);
    }
}
