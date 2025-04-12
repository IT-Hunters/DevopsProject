package com.esprit.tic.twin.springproject.services;

import com.esprit.tic.twin.springproject.entities.Chambre;
import com.esprit.tic.twin.springproject.entities.TypeChambre;
import com.esprit.tic.twin.springproject.repositories.ChambreRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class ChambreServiceImpl implements IChambreService {
    ChambreRepository chambreRepository;

    @Override
    public long nbChambreParTypeEtBloc(TypeChambre type, long idBloc) {
        return chambreRepository.findChambreByBloc_IdBlocAndTypeC(idBloc, type).size();
    }

    @Override
    public Map<String, Float> pourcentageChambreParTypeChambre() {
        List<Chambre> chambres = chambreRepository.findAll();
        float total = chambres.size();
        if (total == 0) {
            return Map.of(
                    TypeChambre.SIMPLE.name(), 0f,
                    TypeChambre.DOUBLE.name(), 0f,
                    TypeChambre.TRIPLE.name(), 0f
            );
        }
        Float simpleCount = chambreRepository.getNbrTypeC(TypeChambre.SIMPLE);
        Float doubleCount = chambreRepository.getNbrTypeC(TypeChambre.DOUBLE);
        Float tripleCount = chambreRepository.getNbrTypeC(TypeChambre.TRIPLE);

        Map<String, Float> result = new HashMap<>();
        result.put(TypeChambre.SIMPLE.name(), (simpleCount * 100) / total);
        result.put(TypeChambre.DOUBLE.name(), (doubleCount * 100) / total);
        result.put(TypeChambre.TRIPLE.name(), (tripleCount * 100) / total);

        log.info("Simple: {}%", result.get(TypeChambre.SIMPLE.name()));
        log.info("Double: {}%", result.get(TypeChambre.DOUBLE.name()));
        log.info("Triple: {}%", result.get(TypeChambre.TRIPLE.name()));

        return result;
    }

    @Override
    public void nbPlacesDisponiblesParChambreAnneeEnCours() {
        List<Chambre> chambres = chambreRepository.findChambreByReservations_AnneeUniversitaire_Year(Year.now().getValue());
        chambres.forEach(c -> {
            int places = switch (c.getTypeC()) {
                case SIMPLE -> 1 - c.getReservations().size();
                case DOUBLE -> 2 - c.getReservations().size();
                case TRIPLE -> 3 - c.getReservations().size();
            };
            log.info("Chambre {} has {} places available for year {}",
                    c.getNumeroChambre(), places, Year.now().getValue());
        });
    }

    @Override
    public List<Chambre> retrieveAllChambres() {
        return chambreRepository.findAll();
    }

    @Override
    public List<Chambre> getChambresParNomBloc(String nomBloc) {
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

    @Override
    public List<Chambre> findAvailableChambresForCapacity(int minCapacity, TypeChambre type) {
        if (minCapacity < 0) {
            throw new IllegalArgumentException("La capacité minimale ne peut pas être négative");
        }
        List<Chambre> chambres = chambreRepository.findChambreByTypeC(type);
        return chambres.stream()
                .filter(chambre -> {
                    int maxCapacity = switch (chambre.getTypeC()) {
                        case SIMPLE -> 1;
                        case DOUBLE -> 2;
                        case TRIPLE -> 3;
                    };
                    int reserved = chambre.getReservations().size();
                    return (maxCapacity - reserved) >= minCapacity;
                })
                .toList();
    }
}