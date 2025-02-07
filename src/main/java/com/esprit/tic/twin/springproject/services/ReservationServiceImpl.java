package com.esprit.tic.twin.springproject.services;

import com.esprit.tic.twin.springproject.entities.Chambre;
import com.esprit.tic.twin.springproject.entities.Etudiant;
import com.esprit.tic.twin.springproject.entities.Reservation;
import com.esprit.tic.twin.springproject.entities.TypeChambre;
import com.esprit.tic.twin.springproject.repositories.ChambreRepository;
import com.esprit.tic.twin.springproject.repositories.EtudiantRepository;
import com.esprit.tic.twin.springproject.repositories.ReservationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class ReservationServiceImpl implements IReservationService {
    ReservationRepository reservationRepository;
    ChambreRepository chambreRepository;
    EtudiantRepository etudiantRepository;
    @Override
    public List<Reservation> getReservationParAnneeUniversitaire(Date dateDebut, Date dateFin ){
        return reservationRepository.findByAnneeUniversitaireBetween(dateDebut,dateFin);
    }

    @Override
    public Reservation ajouterReservationEtAssignerAChambreEtAEtudiant(Reservation res, Long numChambre, long cin) {
        Chambre ch = chambreRepository.findByNumeroChambre(numChambre).orElse(null);
       // int nbrResValid = chambreRepository.countRes(ch);
        Etudiant et = etudiantRepository.findByCin(cin).orElse(null);
        int Capacite = 0;
        if (ch.getTypeC() == TypeChambre.SIMPLE) Capacite = 1;
        if (ch.getTypeC() == TypeChambre.DOUBLE) Capacite = 2;
        if (ch.getTypeC() == TypeChambre.TRIPLE) Capacite = 3;
        if (res.getAnneeUniversitaire().getYear() == Year.now().getValue()
    /*    &&
                Capacite - nbrResValid >= 1*/
        ) {
            String id = numChambre + cin + res.getAnneeUniversitaire().toString();
            res.setIdReservation(id);
            List<Reservation> listR = ch.getReservations().stream().toList();
            listR.add(res);
            List<Reservation> listR1 = et.getReservations().stream().toList();
            listR1.add(res);

        }

        return res;
    }

    @Override
    public List<Reservation> retrieveAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation addReservation(Reservation r) {
        return reservationRepository.save(r);
    }

    @Override
    public Reservation updateReservation(Reservation r) {
        return reservationRepository.save(r);
    }

    @Override
    public Reservation retrieveReservation(String idReservation) {
        return reservationRepository.findById(idReservation).orElse(null);
    }

    @Override
    public void removeReservation(String idReservation) {
        reservationRepository.deleteById(idReservation);
    }
}
