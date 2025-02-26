package com.esprit.tic.twin.springproject.services;

import com.esprit.tic.twin.springproject.entities.Bloc;
import com.esprit.tic.twin.springproject.entities.Chambre;
import com.esprit.tic.twin.springproject.repositories.BlocRepository;
import com.esprit.tic.twin.springproject.repositories.ChambreRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Block;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class BlocServiceImpl implements IBlocService{
    BlocRepository blocRepository;
    ChambreRepository chambreRepository;
    public Bloc affecterChambresABloc(List<Long> numChambre, String nomBloc){
        Bloc bloc = blocRepository.findByNomBloc(nomBloc);
        List<Chambre> listeChambre = numChambre.stream()
                        .map(num -> chambreRepository.findById(num).get())
                                .collect(Collectors.toList());
        listeChambre.forEach(ch -> ch.setBloc(bloc));
        chambreRepository.saveAll(listeChambre);
        return bloc;
    }

   // @Scheduled(cron= "*/2 * * * * *")
    public void listeChambresParBloc(){
        List<Bloc> listB = blocRepository.findAll();
        for (Bloc bloc : listB) {
            log.info("Capacite Bloc" + bloc.getCapaciteBloc());
            for (Chambre ch : bloc.getChambres()){
                log.info("NumChambre " + ch.getNumeroChambre());
                log.info("Type " + ch.getTypeC());
            }
        }
    }

    @Override
    public List<Bloc> retrieveAllBlocs() {
        return blocRepository.findAll();
    }

    @Override
    public Bloc addBloc(Bloc b) {
        return blocRepository.save(b);
    }

    @Override
    public Bloc updateBloc(Bloc b) {
        return blocRepository.save(b);
    }

    @Override
    public Bloc retrieveBloc(Long idBloc) {
        return blocRepository.findById(idBloc).orElse(null);
    }

    @Override
    public void removeBloc(Long idBloc) {
        blocRepository.deleteById(idBloc);
    }

    @Override
    public List<Bloc> retrieveBlocsJPQL(String s) {
        return blocRepository.retrieveBlocs(s);
    }

    @Override
    public List<Bloc> retrieveBlocsKeywords(String s) {
        return blocRepository.findByFoyerUniversiteNomUniversite(s);
    }
}
